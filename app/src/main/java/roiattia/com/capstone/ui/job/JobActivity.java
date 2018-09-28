package roiattia.com.capstone.ui.job;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import iammert.com.expandablelib.ExpandCollapseListener;
import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.database.entry.JobEntry;
import roiattia.com.capstone.model.ExpandableListChild;
import roiattia.com.capstone.model.ExpandableListParent;
import roiattia.com.capstone.repositories.CategoriesRepository;
import roiattia.com.capstone.repositories.ExpensesRepository;
import roiattia.com.capstone.repositories.JobsRepository;
import roiattia.com.capstone.ui.dialogs.EditTextDialog;
import roiattia.com.capstone.ui.dialogs.RadioButtonsDialog;
import roiattia.com.capstone.ui.expense.ExpenseActivity;
import roiattia.com.capstone.utils.AmountUtils;
import roiattia.com.capstone.utils.DateUtils;

import static roiattia.com.capstone.utils.Constants.EXPENSE_FOR_RESULT;
import static roiattia.com.capstone.utils.Constants.EXPENSE_ID_REQUEST;
import static roiattia.com.capstone.utils.Constants.JOB_DATE;
import static roiattia.com.capstone.utils.Constants.JOB_ID_UPDATE;
import static roiattia.com.capstone.utils.Constants.JOB_PAYMENT_DATE;

public class JobActivity extends AppCompatActivity
    implements CategoriesRepository.OnCategoryListener, ExpensesRepository.OnExpenseListener,
        JobsRepository.OnJobListener, RadioButtonsDialog.NoticeDialogListener,
        EditTextDialog.EditTextDialogListener{

    private static final long DEFAULT_JOB_ID = -1;

    private List<CategoryEntry> mCategoriesList;
    private List<ExpenseEntry> mExpenses;
    private LocalDate mJobDate;
    private LocalDate mJobPaymentDate;
    private double mJobIncome;
    private double mJobExpense;
    private double mJobProfit;
    private long mCategoryId;
    private String mCategoryName;
    private String mDescription;
    private long mJobId;

    private JobViewModel mViewModel;
    private Section<ExpandableListParent, ExpandableListChild> mSection;
    private List<ExpandableListChild> mExpandableListChilds;
    private List<Long> mExpensesIds;
    ExpandableListParent mParent;

    @BindView(R.id.rb_existed_category) RadioButton mExistedCategoryButton;
    @BindView(R.id.rb_new_category) RadioButton mNewCategoryButton;
    @BindView(R.id.iet_job_description) TextInputEditText mDescriptionView;
    @BindView(R.id.iet_job_date) TextInputEditText mJobDateView;
    @BindView(R.id.iet_job_payment_date) TextInputEditText mPaymentDateView;
    @BindView(R.id.iet_job_income) TextInputEditText mIncomeView;
    @BindView(R.id.btn_add_expense) Button mAddExpenseButton;
    @BindView(R.id.el_expenses_list) ExpandableLayout mExpensesLayout;
    @BindView(R.id.tv_income) TextView mIncomeTextView;
    @BindView(R.id.tv_expenses) TextView mExpensesTextView;
    @BindView(R.id.tv_profits) TextView mProfitsTextView;

    @OnClick(R.id.btn_add_expense)
    public void addExpense(){
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivityForResult(intent, EXPENSE_ID_REQUEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job2);
        ButterKnife.bind(this);

        mExpensesIds = new ArrayList<>();
        mExpandableListChilds = new ArrayList<>();

        setupViewModel();

        setupUI();

        setupExpandableList();

        // check for data passed: date or job_id for update
        Intent intent = getIntent();
        if(intent != null){
            // check for date
            if(intent.hasExtra(JOB_DATE)) {
                String dateAsString = intent.getStringExtra(JOB_DATE);
                LocalDate localDate = new LocalDate(dateAsString);
                mJobDate = localDate;
                mJobDateView.setText(DateUtils.getDateStringFormat(localDate));
            }
            // check for job_id for update
            if(intent.hasExtra(JOB_ID_UPDATE)){
                mJobId = intent.getLongExtra(JOB_ID_UPDATE, DEFAULT_JOB_ID);
                mViewModel.setMutableLiveJob(mJobId);
                setTitle("Update Job");
            } else {
                setTitle("New Job");
            }
        }
    }

    private void setupExpandableList() {
        mExpensesLayout.setRenderer(new ExpandableLayout.Renderer<ExpandableListParent, ExpandableListChild>() {
            @Override
            public void renderParent(View view, ExpandableListParent parent, boolean b, int i) {
                TextView expensesAmountText = view.findViewById(R.id.tv_parent_expenses);
                TextView expensesNumberText = view.findViewById(R.id.tv_parent_number_of_expenses);
                expensesAmountText.setText(String.format("Total expenses amount: %s",
                        AmountUtils.getStringFormatFromDouble(parent.getExpensesAmount())));
                expensesNumberText.setText(String.format("Number of expenses: %s",
                        String.valueOf(parent.getNumberOfExpenses())));
            }

            @Override
            public void renderChild(final View view, final ExpandableListChild child, final int i, final int i1) {
                ButterKnife.bind(this, view);
                TextView categoryName = view.findViewById(R.id.tv_expandalbe_child_category);
                TextView categoryAmount = view.findViewById(R.id.tv_expandalbe_child_amount);
                ImageButton delete = view.findViewById(R.id.btn_expandable_child_delete);
                ImageButton edit = view.findViewById(R.id.btn_expandable_child_edit);
                categoryName.setText(String.format("%s - %s", child.getCategoryName(), child.getDescription()));
                categoryAmount.setText(AmountUtils.getStringFormatFromDouble(child.getCost()));
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.deleteExpenseById(child.getExpenseId());
                        mExpenses.remove(i1);
                        mExpandableListChilds.remove(i1);
                        updateJobFinances();
                        updateExpandableList();
                    }
                });
            }
        });

        mExpensesLayout.setExpandListener(new ExpandCollapseListener.ExpandListener<ExpandableListParent>() {
            @Override
            public void onExpanded(int parentIndex, ExpandableListParent parent, View view) {
                //Layout expanded
                ImageButton imageButton = view.findViewById(R.id.ib_drop_list);
                imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_drop_down));
            }
        });

        mExpensesLayout.setCollapseListener(new ExpandCollapseListener.CollapseListener<ExpandableListParent>() {
            @Override
            public void onCollapsed(int parentIndex, ExpandableListParent parent, View view) {
                //Layout collapsed
                ImageButton imageButton = view.findViewById(R.id.ib_drop_list);
                imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_drop_up));
            }
        });

        mExpenses = new ArrayList<>();
        mSection = new Section<>();
        mParent = new ExpandableListParent(mJobExpense, mExpenses.size());
        mSection.parent = mParent;
        mExpensesLayout.addSection(mSection);
    }

    private void updateExpandableList() {

    }

    private void setupUI() {
        // setup job_date and payment_date with on_click_listeners
        mJobDateView.setFocusable(false);
        mPaymentDateView.setFocusable(false);
        mJobDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDatePicker(mJobDateView, JOB_DATE);
            }
        });
        mPaymentDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDatePicker(mPaymentDateView, JOB_PAYMENT_DATE);
            }
        });

        // setup fee change listener
        mIncomeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    mJobIncome = AmountUtils.getDoubleFormatFromString(s.toString());
                    updateSummary();
                }
            }
        });
    }

    private void setupDatePicker(final EditText jobDateView, final String dateType) {
        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateString = year + "-" + (month+1) + "-" + dayOfMonth;
                        LocalDate localDate = new LocalDate(dateString);
                        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
                        jobDateView.setText(fmt.print(localDate));
                        if(dateType.equals(JOB_DATE)) mJobDate = localDate;
                        if(dateType.equals(JOB_PAYMENT_DATE)) mJobPaymentDate = localDate;
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(JobViewModel.class);
        mViewModel.getLiveDataCategories().observe(this, new Observer<List<CategoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                mViewModel.getLiveDataCategories().removeObserver(this);
                if (categoryEntries != null) {
                    mCategoriesList = categoryEntries;
                    setupRadioButtons(categoryEntries);
                }
            }
        });
        mViewModel.getMutableLiveJob().observe(this, new Observer<JobEntry>() {
            @Override
            public void onChanged(@Nullable JobEntry jobEntry) {
                if(jobEntry != null) {
                    updateUiWithJobDetails(jobEntry);
                }
            }
        });
        mViewModel.getMutableLiveExpense().observe(this, new Observer<ExpenseEntry>() {
            @Override
            public void onChanged(@Nullable ExpenseEntry expenseEntry) {
                if(expenseEntry != null) {
                    mExpenses.add(expenseEntry);
                    mViewModel.setMutableLiveCategory(expenseEntry.getCategoryId());
                    updateJobFinances();
                }
            }
        });
        mViewModel.getMutableLiveCategory().observe(this, new Observer<CategoryEntry>() {
            @Override
            public void onChanged(@Nullable CategoryEntry categoryEntry) {
                if(categoryEntry != null) {
                    ExpenseEntry expenseEntry = mExpenses.get(mExpenses.size()-1);
                    ExpandableListChild child = new ExpandableListChild(expenseEntry.getExpenseId(),
                            categoryEntry.getCategoryName(), expenseEntry.getDescription(),
                            expenseEntry.getExpenseCost());
                    mExpandableListChilds.add(child);
                    updateExpensesList();
                }
            }
        });

    }

    private void updateJobFinances() {
        mJobExpense = 0;
        for(ExpenseEntry expenseEntry : mExpenses)
            mJobExpense += expenseEntry.getExpenseCost();
        updateSummary();
    }

    private void updateExpensesList() {
        mSection.children.clear();
        mSection.children.addAll(mExpandableListChilds);
        mParent = new ExpandableListParent(mJobExpense, mExpenses.size());
        mSection.parent = mParent;
        mExpensesLayout.notifyParentChanged(0);
    }

    private void updateUiWithJobDetails(JobEntry jobEntry) {
        mJobId = jobEntry.getJobId();
        mCategoryId = jobEntry.getCategoryId();
        mDescription = jobEntry.getJobDescription();
        mDescriptionView.setText(mDescription);
        mJobDate = jobEntry.getJobDate();
        mJobDateView.setText(DateUtils.getDateStringFormat(mJobDate));
        mJobPaymentDate = jobEntry.getJobDateOfPayment();
        mPaymentDateView.setText(DateUtils.getDateStringFormat(mJobPaymentDate));
        mJobIncome = jobEntry.getJobIncome();
        mIncomeView.setText(AmountUtils.getStringFormatFromDouble(mJobIncome));
        mExistedCategoryButton.setChecked(true);
        mJobExpense = jobEntry.getJobExpenses();
        mJobProfit = jobEntry.getJobProfits();
        // TODO: load expenses
        updateSummary();
    }

    private void updateSummary() {
        mJobProfit = mJobIncome - mJobExpense;
        mIncomeTextView.setText(String.format("%s %s", "Income:",
                AmountUtils.getStringFormatFromDouble(mJobIncome)));
        mExpensesTextView.setText(String.format("%s %s", "Expenses:",
                AmountUtils.getStringFormatFromDouble(mJobExpense)));
        mProfitsTextView.setText(String.format("%s %s", "Profits:",
                AmountUtils.getStringFormatFromDouble(mJobProfit)));
    }

    private void setupRadioButtons(List<CategoryEntry> categoryEntries) {
        final List<String> categoriesNames = new ArrayList<>();
        for(CategoryEntry categoryEntry : categoryEntries){
            categoriesNames.add(categoryEntry.getCategoryName());
        }

        // setup category radio buttons
        mExistedCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewCategoryButton.setChecked(false);
                RadioButtonsDialog categoryPicker = new RadioButtonsDialog();
                categoryPicker.setTitle(getString(R.string.pick_category_title));
                String[] categories = new String[categoriesNames.size()];
                categories = categoriesNames.toArray(categories);
                categoryPicker.setData(categories);
                categoryPicker.show(getSupportFragmentManager(), "existing_category");
            }
        });

        mNewCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextDialog dialog = new EditTextDialog();
                dialog.setTitle(getString(R.string.enter_category_title));
                dialog.show(getSupportFragmentManager(), "new_category");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm_job_expense, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mi_done){
            // check if all needed input exists
            if(checkInputValidation()) {
                Toast.makeText(this, R.string.job_saved_toast_message, Toast.LENGTH_LONG).show();
                // check if user entered description
                if(mDescriptionView.getText() != null){
                    mDescription = mDescriptionView.getText().toString();
                }
                // check if a new category needs to insert
                if(mNewCategoryButton.isChecked()){
                    mViewModel.insertNewCategory(mCategoryName ,this);
                } else {
                    mViewModel.insertJob(mJobId, mCategoryId, mDescription, mJobDate, mJobPaymentDate,
                            mJobIncome, mJobExpense, mJobProfit, this);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkInputValidation() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EXPENSE_ID_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                long expenseId = data.getLongExtra(EXPENSE_FOR_RESULT, 0);
                mViewModel.setMutableLiveExpense(expenseId);
            }
        }
    }

    @Override
    public void onCategoryInserted(long categoryId) {
        mCategoryId = categoryId;
        mViewModel.insertJob(mJobId, mCategoryId, mDescription, mJobDate, mJobPaymentDate, mJobIncome,
                mJobExpense, mJobProfit, this);
    }

    @Override
    public void onJobInserted(long jobId) {
        for(ExpenseEntry expenseEntry : mExpenses)
            expenseEntry.setJobId(jobId);
        mViewModel.insertExpense(mExpenses);
        finish();
    }

    @Override
    public void onExpenseInserted(long expenseId) {
        // TODO: not sure if needed
    }

    @Override
    public void onDialogFinishClick(String input) {
        mCategoryName = input;
        mNewCategoryButton.setText(String.format("%s - %s", getString(R.string.enter_new_category),
                mCategoryName));
    }

    @Override
    public void onDialogFinishClick(int whichSelected) {
        mCategoryId = mCategoriesList.get(whichSelected).getCategoryId();
        mExistedCategoryButton.setText(String.format("%s - %s", getString(R.string.pick_from_existing_category),
                mCategoriesList.get(whichSelected).getCategoryName()));
    }
}
