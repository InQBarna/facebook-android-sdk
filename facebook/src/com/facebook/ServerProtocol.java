/**
 * Copyright 2012 Facebook
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook;

import java.util.Collection;

final class ServerProtocol {
    static final String FACEBOOK_COM = "facebook.com";
    static final String DIALOG_AUTHORITY = "m." + FACEBOOK_COM;
    static final String DIALOG_PATH = "dialog/";
    static final String DIALOG_OAUTH_PATH = DIALOG_PATH + "oauth";
    static final String DIALOG_PARAM_SCOPE = "scope";
    static final String DIALOG_PARAM_CLIENT_ID = "client_id";
    static final String DIALOG_PARAM_DISPLAY = "display";
    static final String DIALOG_PARAM_REDIRECT_URI = "redirect_uri";
    static final String DIALOG_PARAM_TYPE = "type";

    // URL components
    static final String GRAPH_URL = "https://graph." + FACEBOOK_COM;
    static final String GRAPH_URL_BASE = "https://graph." + FACEBOOK_COM + "/";
    static final String REST_URL_BASE = "https://api." + FACEBOOK_COM + "/method/";
    static final String BATCHED_REST_METHOD_URL_BASE = "method/";

    static final Collection<String> errorsProxyAuthDisabled =
            Utility.unmodifiableCollection("service_disabled", "AndroidAuthKillSwitchException");
    static final Collection<String> errorsUserCanceled =
            Utility.unmodifiableCollection("access_denied", "OAuthAccessDeniedException");
}
