package com.ek.myassignment.widgets;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ReportLayout extends RelativeLayout {

    public final String TAG = ReportLayout.class.getSimpleName();

    // set the header titles
    private String headers[] = null;

    private TableLayout tableB;
    private TableLayout tableD;

    private HorizontalScrollView horizontalScrollViewB;
    private HorizontalScrollView horizontalScrollViewD;

    private ScrollView scrollViewD;

    private Context context;
    private String[] sampleObjects = null;
    private int headerCellsWidth[] = new int[20];

    public ReportLayout(Context context) {

        super(context);

        this.context = context;

        // initialize the main components (TableLayouts, HorizontalScrollView, ScrollView)
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();

        // no need to assemble component A, since it is just a table
        this.horizontalScrollViewB.addView(this.tableB);

        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);

        // add the components to be part of the main layout
        this.addComponentToMainLayout();
        this.setBackgroundColor(Color.RED);

    }

    public void addTableValues(String[] sampleObjects, String headers[]) {

        this.sampleObjects = sampleObjects;
        this.headers = headers;
        // add some table rows
        this.addTableRowToTableB();

        this.resizeHeaderHeight();

        this.getTableRowHeaderCellWidth();

        this.generateTableC_AndTable_B();

        this.resizeBodyTableRowHeight();
    }

    // initalized components
    private void initComponents() {

        this.tableB = new TableLayout(this.context);
        this.tableD = new TableLayout(this.context);

        this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);

        this.scrollViewD = new MyScrollView(this.context);
        this.horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);

        horizontalScrollViewB.setVerticalScrollBarEnabled(false);
        horizontalScrollViewB.setHorizontalScrollBarEnabled(false);
    }

    // set essential component IDs
    private void setComponentsId() {
        this.horizontalScrollViewB.setId(2);
        this.scrollViewD.setId(4);
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag() {

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewD.setTag("scroll view d");
    }

    // we add the components here in our TableMainLayout
    private void addComponentToMainLayout() {
        LayoutParams componentB_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        LayoutParams componentD_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(this.scrollViewD, componentD_Params);

    }

    private void addTableRowToTableB() {
        this.tableB.addView(this.componentBTableRow());
    }

    // generate table row of table B
    private TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 0);

        for (int x = 0; x < (headerFieldCount); x++) {
            TextView textView = this.headerTextView(this.headers[x]);
            textView.setLayoutParams(params);
            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }

    // generate table row of table C and table D
    public void generateTableC_AndTable_B() {

        //for (String sampleObject : this.sampleObjects) {
        for (int i = 0; i < sampleObjects.length; i++) {
            TableRow taleRowForTableD = this.taleRowForTableD(sampleObjects[i]);
            taleRowForTableD.setBackgroundColor(Color.LTGRAY);
            this.tableD.addView(taleRowForTableD);

        }
    }

    private TableRow taleRowForTableD(String sampleObject) {

        TableRow taleRowForTableD = new TableRow(this.context);

        int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();

//        String info[] = sampleObject.split(" {1,5}", headers.length);
//        String info[] = new String[headers.length];
        String info[] = sampleObject.split(" +");

        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(x > info.length || info[x].contains("---") || TextUtils.isEmpty(info[x]) ? "N/A" : info[x]);
            taleRowForTableD.addView(textViewB, params);
        }

        return taleRowForTableD;

    }

    // table cell standard TextView
    private TextView bodyTextView(String label) {

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);

        return bodyTextView;
    }

    // header standard TextView
    private TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.WHITE);
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setPadding(20, 5, 20, 5);

        return headerTextView;
    }

    // resizing TableRow height starts here
    private void resizeHeaderHeight() {

        TableRow productInfoTableRow = (TableRow) this.tableB.getChildAt(0);

        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = productInfoTableRow;
        int finalHeight = rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    private void getTableRowHeaderCellWidth() {

        int tableBChildCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();
        ;

        for (int x = 0; x < (tableBChildCount); x++) {
            this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableB.getChildAt(0)).getChildAt(x));
        }
    }

    // resize body table row height
    private void resizeBodyTableRowHeight() {

        int tableC_ChildCount = this.tableD.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {

            TableRow productInfoTableRow = (TableRow) this.tableD.getChildAt(x);

            int rowAHeight = 100;//this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = 80;//this.viewHeight(productInfoTableRow);

            TableRow tableRow = productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if (tableRow.getChildCount() == 1) {

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    // check if the view has the highest height in a TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    // read a view's height
    private int viewHeight(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // horizontal scroll view custom class
    class MyHorizontalScrollView extends HorizontalScrollView {

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();

            if (tag.equalsIgnoreCase("horizontal scroll view b")) {
                horizontalScrollViewD.scrollTo(l, 0);
            } else {
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }

    }

    // scroll view custom class
    class MyScrollView extends ScrollView {

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();

            if (tag.equalsIgnoreCase("scroll view c")) {
                scrollViewD.scrollTo(0, t);
            } else {
                //  scrollViewC.scrollTo(0,t);
            }
        }
    }
}