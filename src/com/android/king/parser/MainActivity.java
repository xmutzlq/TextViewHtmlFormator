package com.android.king.parser;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HtmlTagFormater formater = new HtmlTagFormater();
		formater.handlerHtmlContent(getApplicationContext(), "传入html文本");
	}
}
