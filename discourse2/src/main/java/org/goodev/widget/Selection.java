/***
 Copyright (c) 2008-2011 CommonsWare, LLC

 Licensed under the Apache License, Version 2.0 (the "License"); you may
 not use this file except in compliance with the License. You may obtain
 a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.goodev.widget;

import android.widget.EditText;

import java.io.Serializable;

public class Selection implements Serializable {
    public int start;
    public int end;

    public Selection(int _start, int _end) {
        start = _start;
        end = _end;

        if (start > end) {
            int temp = end;
            end = start;
            start = temp;
        }
    }

    public Selection(EditText editor) {
        this(editor.getSelectionStart(), editor.getSelectionEnd());
    }

    public boolean isEmpty() {
        return (start == end);
    }

    public void apply(EditText editor) {
        editor.setSelection(start, end);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + end;
        result = prime * result + start;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Selection other = (Selection) obj;
        if (end != other.end)
            return false;
        if (start != other.start)
            return false;
        return true;
    }

}
