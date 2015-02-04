/*
 * Copyright (C) 2013 Mohammed Lakkadshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sufficientlysecure.htmltextview;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.TypefaceSpan;

import org.goodev.discourse.utils.L;
import org.xml.sax.XMLReader;

import java.util.Stack;
import java.util.Vector;

public class HtmlTagHandler implements Html.TagHandler {

    /**
     * List indentation in pixels. Nested lists use multiple of this.
     */
    private static final int indent = 10;
    private static final int listItemIndent = indent * 2;
    private static final BulletSpan bullet = new BulletSpan(indent);
    private final int mListItemCount = 0;
    private final Vector<String> mListParents = new Vector<String>();
    /**
     * Keeps track of lists (ol, ul). On bottom of Stack is the outermost list and on top of Stack is the most nested list
     */
    Stack<String> lists = new Stack<String>();
    /**
     * Tracks indexes of ordered lists so that after a nested list ends we can continue with correct index of outer list
     */
    Stack<Integer> olNextIndex = new Stack<Integer>();

    private static void start(Editable text, Object mark) {
        int len = text.length();
        text.setSpan(mark, len, len, Spanned.SPAN_MARK_MARK);
    }

    private static void end(Editable text, Class<?> kind, Object... replaces) {
        int len = text.length();
        Object obj = getLast(text, kind);
        int where = text.getSpanStart(obj);
        text.removeSpan(obj);
        if (where != len) {
            for (Object replace : replaces) {
                text.setSpan(replace, where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return;
    }

    private static Object getLast(Spanned text, Class<?> kind) {
        /*
         * This knows that the last returned object from getSpans() will be the most recently added.
         */
        Object[] objs = text.getSpans(0, text.length(), kind);
        if (objs.length == 0) {
            return null;
        }
        return objs[objs.length - 1];
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("ul")) {
            if (opening) {
                lists.push(tag);
            } else {
                lists.pop();
            }
        } else if (tag.equalsIgnoreCase("ol")) {
            if (opening) {
                lists.push(tag);
                olNextIndex.push(Integer.valueOf(1)).toString();// TODO: add support for lists starting other index than 1
            } else {
                lists.pop();
                olNextIndex.pop().toString();
            }
        } else if (tag.equalsIgnoreCase("li")) {
            if (opening) {
                if (output.length() > 0 && output.charAt(output.length() - 1) != '\n') {
                    output.append("\n");
                }
                String parentList = lists.peek();
                if (parentList.equalsIgnoreCase("ol")) {
                    start(output, new Ol());
                    output.append(olNextIndex.peek().toString() + ". ");
                    olNextIndex.push(Integer.valueOf(olNextIndex.pop().intValue() + 1));
                } else if (parentList.equalsIgnoreCase("ul")) {
                    start(output, new Ul());
                }
            } else {
                if (lists.peek().equalsIgnoreCase("ul")) {
                    if (output.charAt(output.length() - 1) != '\n') {
                        output.append("\n");
                    }
                    // Nested BulletSpans increases distance between bullet and text, so we must prevent it.
                    int bulletMargin = indent;
                    if (lists.size() > 1) {
                        bulletMargin = indent - bullet.getLeadingMargin(true);
                        if (lists.size() > 2) {
                            // This get's more complicated when we add a LeadingMarginSpan into the same line:
                            // we have also counter it's effect to BulletSpan
                            bulletMargin -= (lists.size() - 2) * listItemIndent;
                        }
                    }
                    BulletSpan newBullet = new BulletSpan(bulletMargin);
                    end(output, Ul.class, new LeadingMarginSpan.Standard(listItemIndent * (lists.size() - 1)), newBullet);
                } else if (lists.peek().equalsIgnoreCase("ol")) {
                    if (output.charAt(output.length() - 1) != '\n') {
                        output.append("\n");
                    }
                    int numberMargin = listItemIndent * (lists.size() - 1);
                    if (lists.size() > 2) {
                        // Same as in ordered lists: counter the effect of nested Spans
                        numberMargin -= (lists.size() - 2) * listItemIndent;
                    }
                    end(output, Ol.class, new LeadingMarginSpan.Standard(numberMargin));
                }
            }
        } else if (tag.equalsIgnoreCase("code")) {
            if (opening) {
                output.setSpan(new TypefaceSpan("monospace"), output.length(), output.length(), Spannable.SPAN_MARK_MARK);
            } else {
                L.d("Code tag encountered");
                Object obj = getLast(output, TypefaceSpan.class);
                int where = output.getSpanStart(obj);
                output.setSpan(new TypefaceSpan("monospace"), where, output.length(), 0);
                output.setSpan(new BackgroundColorSpan(Color.parseColor("#f1f1ff")), where, output.length(), 0);
            }
        } else {
            if (opening)
                L.d("Found an unsupported tag " + tag);
        }
    }

    private static class Ul {
    }

    private static class Ol {
    }

}
