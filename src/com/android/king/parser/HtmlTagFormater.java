package com.android.king.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;

/**
 * 拓展TextView->HTML特殊属�?�支�? <p>
 * 
 * 不支持图�?
 * 
 * @author zlq
 * @date 2016�?6�?29�? 上午9:35:58
 */
public class HtmlTagFormater {

	private static final String TAG_HANDLE_SPAN = "span";
	private static final String TAG_HANDLE_STYLE = "style";
	private static final String TAG_HANDLE_ALIGN = "align";
	
	private static final String TAG_FONT_SIZE = "font-size";
	private static final String TAG_BACKGROUND_COLOR = "background-color";
	private int startIndex;
	private int stopIndex;
	private String styleContent = "";
	
	public Spanned handlerHtmlContent(final Context context, String htmlContent) throws NumberFormatException {
		return HtmlParser.buildSpannedText(htmlContent, new HtmlParser.TagHandler() {
			@Override
			public boolean handleTag(boolean opening, String tag, Editable output, Attributes attributes) {
				if(tag.equals(TAG_HANDLE_SPAN)) {
					if(opening) {
						startIndex = output.length();
						styleContent = HtmlParser.getValue(attributes, TAG_HANDLE_STYLE);
					} else {
						stopIndex = output.length();
						if (!TextUtils.isEmpty(styleContent)) {
							String[] styleValues = styleContent.split(";"); 
							for (String styleValue : styleValues) {
								String[] tmpValues = styleValue.split(":");
								if (tmpValues != null && tmpValues.length > 0) { //�?要标�?+数据才可食用(font-size=14px)
									 if(TAG_FONT_SIZE.equals(tmpValues[0])) { //处理文字效果
										 int size = Integer.valueOf(getAllNumbers(tmpValues[1]));
										 output.setSpan(new AbsoluteSizeSpan(size), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									 } else if(TAG_BACKGROUND_COLOR.equals(tmpValues[0])) { //处理背景效果
										 output.setSpan(new BackgroundColorSpan(Color.parseColor(tmpValues[1])), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									 }
								}
							}
						}
					}
				} else if(tag.equals(TAG_HANDLE_ALIGN)) {
					if(opening) {
						startIndex = output.length();
					} else {
						stopIndex = output.length();
					}
				}
				return false;
			}
		});
	}
	
	private static String getAllNumbers(String body) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(body);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}
}
