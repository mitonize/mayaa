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
package org.seasar.maya.provider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.maya.builder.SpecificationBuilder;
import org.seasar.maya.builder.TemplateBuilder;
import org.seasar.maya.cycle.Application;
import org.seasar.maya.cycle.ServiceCycle;
import org.seasar.maya.cycle.script.ScriptCompiler;
import org.seasar.maya.engine.Engine;

/**
 * アプリケーションスコープでのサービス提供オブジェクト。
 * @author Masataka Kurihara (Gluegent, Inc.)
 */
public interface ServiceProvider {

    /**
     * アプリケーションコンテキストの取得。
     * @return アプリケーションコンテキスト。
     */
    Application getApplication();
    
    /**
     * エンジンの生成を行う。
     * @return	エンジン
     */
    Engine getEngine();
    
    /**
     * スクリプトコンパイラの取得。
     * @return スクリプトコンパイラ。
     */
    ScriptCompiler getScriptCompiler();
    
    /**
     * 設定XMLのビルダを取得する。
     * @return 設定XMLビルダ。
     */
    SpecificationBuilder getSpecificationBuilder();
	
    /**
     * HTMLテンプレートファイルのビルダを取得する。
     * @return テンプレートビルダ。
     */
    TemplateBuilder getTemplateBuilder();

    /**
     * サーブレットAPIのコンテキストオブジェクト設定。
     * @param request カレントのHTTPリクエスト。
     * @param response カレントのHTTPレスポンス。
     */
    void initialize(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * サービスサイクルの取得
     * @return カレントスレッドでのサービスサイクル。
     */
    ServiceCycle getServiceCycle();

    /**
     * ユーザー定義のモデルオブジェクト取得メソッド。
     * @param modelKey コンポーネントキー。
     * @param modelScope スコープ。
     * @return モデルオブジェクト。
     */
    Object getModel(Object modelKey, String modelScope);
    
}
