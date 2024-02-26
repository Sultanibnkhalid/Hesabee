package com.example.countsksh.helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.countsksh.R;

import org.mariuszgromada.math.mxparser.Expression;


public class CalculatorHelper {

    private EditText editText;
    private Context context;
    private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0,btnDot,btnMul,btnDiv,btnAdd,btnSub,btnClear,btnEqual,btnOk;
    private Button btnParthClose,btnParthOpen;
    private   ImageButton iBtnDelete;
    private Dialog dialog;
    /**
     *
     * @param context type Context
     * necessary to show the dialog and get the resources
     */
    public CalculatorHelper(Context context){
        this.context=context;

    }


    /**
     *
     * @param etTarget type ofEditText
     * show the calculator dialog and
     * sets calculator result to   etTarget
     */
    public void show(EditText etTarget){


        etTarget.setText("");

       dialog =new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.calculator_dialog_layout);

        btn0=dialog.findViewById(R.id.btn0);
        btn1=dialog.findViewById(R.id.btn1);
        btn2=dialog.findViewById(R.id.btn2);
        btn3=dialog.findViewById(R.id.btn3);
        btn4=dialog.findViewById(R.id.btn4);
        btn5=dialog.findViewById(R.id.btn5);
        btn6=dialog.findViewById(R.id.btn6);
        btn7=dialog.findViewById(R.id.btn7);
        btn8=dialog.findViewById(R.id.btn8);
        btn9=dialog.findViewById(R.id.btn9);

        btnParthClose=dialog.findViewById(R.id.btnParethesesclose);
        btnParthOpen=dialog.findViewById(R.id.btnParetheseOpen);

        btnDiv=dialog.findViewById(R.id.btnDivide);
        btnMul=dialog.findViewById(R.id.btnMult);
        btnAdd=dialog.findViewById(R.id.btnAdd);
        btnSub=dialog.findViewById(R.id.btnSub);

        btnClear=dialog.findViewById(R.id.btnClear);
        btnDot=dialog.findViewById(R.id.btnDot);
        btnEqual=dialog.findViewById(R.id.btnEqual);

        btnOk=dialog.findViewById(R.id.btnOK_calc);

        iBtnDelete=dialog.findViewById(R.id.btnDelete);
        editText=dialog.findViewById(R.id.displayEditText);

        dialog.show();

        //set on click listeners to buttons
        btn0.setOnClickListener(View->zeroBTNPush());
        btn1.setOnClickListener(View->oneBTNPush());
        btn2.setOnClickListener(View->twoBTNPush());
        btn3.setOnClickListener(View->threeBTNPush());
        btn4.setOnClickListener(View->fourBTNPush());
        btn5.setOnClickListener(View->fiveBTNPush());
        btn6.setOnClickListener(View->sixBTNPush());
        btn7.setOnClickListener(View->sevenBTNPush());
        btn8.setOnClickListener(View->eightBTNPush());
        btn9.setOnClickListener(View->nineBTNPush());
        btnAdd.setOnClickListener(View->addBTNPush());
        btnSub.setOnClickListener(View->subBTNPush());
        btnMul.setOnClickListener(View->multBTNPush());
        btnDiv.setOnClickListener(View->diviBTNPush());
        btnDot.setOnClickListener(View->dotBTNPush());

        btnParthOpen.setOnClickListener(View->partheseOpenBTNPush());
        btnParthClose.setOnClickListener(View->parthenseCloseBTNPush());
        btnClear.setOnClickListener(View->clearBTNPush());
        iBtnDelete.setOnClickListener(View->deleteBTNPush());


        //= > equal
        btnEqual.setOnClickListener(View-> {
            equalBTNPush();
            etTarget.setText(editText.getText().toString());
            dialog.dismiss();
        });

        //=>ok
        btnOk.setOnClickListener(View-> {
            equalBTNPush();
           etTarget.setText(editText.getText().toString());
            dialog.dismiss();
        });

    }

    /**
     * @param strToAdd
     *add the text to the edit text button
     */
    private void updateText(String  strToAdd){
        String olderText=editText.getText().toString();
        int cursorPos=editText.getSelectionStart();

        String leftStr=olderText.substring(0,cursorPos);

        String rightStr=olderText.substring(cursorPos);
        editText.setText(String.format("%s%s%s",leftStr,strToAdd,rightStr));
        editText.setSelection(cursorPos+1);
    }

    /**
     * btn0 click function
     */
    public  void  zeroBTNPush(){

        updateText(context.getResources().getString(R.string.zeroText));
    }
    /**
     * btn1 click function
     */
    public  void  oneBTNPush(){

        updateText(context.getResources().getString(R.string.oneText));
    }
    /**
     * bt2 click function
     */
    public  void  twoBTNPush(){

        updateText(context.getResources().getString(R.string.twoText));
    }
    /**
     * btn3 click function
     */
    public  void  threeBTNPush(){

        updateText(context.getResources().getString(R.string.threeText));
    }
    /**
     * btn4 click function
     */
    public  void  fourBTNPush(){

        updateText(context.getResources().getString(R.string.fourText));
    }
    /**
     * bt5 click function
     */
    public  void  fiveBTNPush(){

        updateText(context.getResources().getString(R.string.fiveText));
    }
    /**
     * btn6 click function
     */
    public  void  sixBTNPush(){

        updateText(context.getResources().getString(R.string.sixText));
    }
    /**
     * btn7 click function
     */
    public  void  sevenBTNPush(){

        updateText(context.getResources().getString(R.string.sevenText));
    }
    /**
     * btn8 click function
     */
    public  void  eightBTNPush(){

        updateText(context.getResources().getString(R.string.eightText));
    }
    /**
     * btn9click function
     */
    public  void  nineBTNPush(){

        updateText(context.getResources().getString(R.string.nineText));
    }
    /**
     * btnAdd click function
     */
    public  void  addBTNPush(){

        updateText(context.getResources().getString(R.string.addationText));
    }
    /**
     * btnSub click function
     */
    public  void  subBTNPush(){

        updateText(context.getResources().getString(R.string.subtractionText));
    }
    /**
     * btnMul click function
     */
    public  void  multBTNPush(){

        updateText(context.getResources().getString(R.string.multiplateText));
    }
    /**
     * btnDot click function
     */
    public  void  dotBTNPush(){

        updateText(context.getResources().getString(R.string.dotText));
    }
    /**
     * btnParthensClose click function
     */
    public  void  parthenseCloseBTNPush(){

        updateText(context.getResources().getString(R.string.parenthesesCloseText));
    }
    /**
     * btnParthens click function
     */
    public  void  partheseOpenBTNPush(){

        updateText(context.getResources().getString(R.string.parenthesesOpenText));
    }
    /**
     * btnDiv click function
     */
    public  void  diviBTNPush(){

        updateText(context.getResources().getString(R.string.dinideText));
    }

    /**
     * btnClear click function
     *
     */
    public  void  clearBTNPush(){
        editText.setText("");


    }
    /**
     * btnDelete click function
     *
     */
    public  void deleteBTNPush(){

        int cursorPos = editText.getSelectionStart();
        int textLen = editText.getText().length();
        if(cursorPos!=0 && textLen!=0){
            SpannableStringBuilder selection=(SpannableStringBuilder) editText.getText();
            selection.replace(cursorPos-1,cursorPos,"");
           editText.setText(selection);
            editText.setSelection(cursorPos-1);
        }


    }
    /**
     * btnEqual click function
     * gets the expression result and
     * sets it to the edit text
     */
    public  void  equalBTNPush(){

        //get the exp... string
        String userExp=editText.getText().toString();

        //replace the symbols
        userExp=userExp.replaceAll("÷","/");
        userExp=userExp.replaceAll("×","*");

        //define exp...
        Expression expression=new Expression(userExp);
        //calculate exp
        String result=String.valueOf(expression.calculate());
        //put the result
        editText.setText(result);
        editText.setSelection(result.length());



    }





















}
