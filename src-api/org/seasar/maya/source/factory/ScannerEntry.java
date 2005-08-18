/*
 * Copyright (c) 2004-2005 the Seasar Foundation and the Others.
 *
 * Licensed under the Seasar Software License, v1.1 (aka "the License");
 * you may not use this file except in compliance with the License which
 * accompanies this distribution, and is available at
 *
 *     http://www.seasar.org/SEASAR-LICENSE.TXT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.seasar.maya.source.factory;

import org.seasar.maya.provider.Parameterizable;
import org.seasar.maya.source.SourceScanner;

/**
 * スキャナ毎のファクトリエントリ。  
 */
public interface ScannerEntry extends Parameterizable {
    
    /**
     * プロトコル名の取得。
     * @return プロトコル名。
     */
    String getProtocol();
    
    /**
     * ソーススキャナの生成。
     * @param systemID スキャナのSystemID。
     * @return ソーススキャナ。
     */
    SourceScanner createSourceScanner(String systemID);
    
}