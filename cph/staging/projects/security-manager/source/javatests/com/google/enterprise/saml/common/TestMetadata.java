// Copyright 2008 Google Inc.  All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.enterprise.saml.common;

import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml2.metadata.SPSSODescriptor;

import static com.google.enterprise.saml.common.OpenSamlUtil.SM_ISSUER;
import static com.google.enterprise.saml.common.OpenSamlUtil.makeArtifactResolutionService;
import static com.google.enterprise.saml.common.OpenSamlUtil.makeAssertionConsumerService;
import static com.google.enterprise.saml.common.OpenSamlUtil.makeEntityDescriptor;
import static com.google.enterprise.saml.common.OpenSamlUtil.makeIdpSsoDescriptor;
import static com.google.enterprise.saml.common.OpenSamlUtil.makeSingleSignOnService;
import static com.google.enterprise.saml.common.OpenSamlUtil.makeSpSsoDescriptor;

import static org.opensaml.common.xml.SAMLConstants.SAML2_ARTIFACT_BINDING_URI;
import static org.opensaml.common.xml.SAMLConstants.SAML2_REDIRECT_BINDING_URI;
import static org.opensaml.common.xml.SAMLConstants.SAML2_SOAP11_BINDING_URI;

/**
 * Metadata for SAML tests
 */
public final class TestMetadata {

  public static final String MOCK_GSA_ISSUER = "http://google.com/enterprise/mock-gsa";

  private static final String URL_PREFIX = "http://localhost:8973/security-manager/";
  private static final String MOCK_SP_URL = URL_PREFIX + "mockserviceprovider";
  private static final String MOCK_ACS_URL = URL_PREFIX + "mockartifactconsumer";
  private static final String SSO_URL = URL_PREFIX + "samlauthn";
  private static final String AR_URL = URL_PREFIX + "samlartifact";

  public static void initializeMetadata() {
    EntityDescriptor spEntity = makeEntityDescriptor(MOCK_GSA_ISSUER);
    SPSSODescriptor sp = makeSpSsoDescriptor(spEntity);
    makeAssertionConsumerService(sp, SAML2_ARTIFACT_BINDING_URI, MOCK_ACS_URL).setIsDefault(true);
    
    EntityDescriptor smEntity = makeEntityDescriptor(SM_ISSUER);
    IDPSSODescriptor idp = makeIdpSsoDescriptor(smEntity);
    makeSingleSignOnService(idp, SAML2_REDIRECT_BINDING_URI, SSO_URL);
    makeArtifactResolutionService(idp, SAML2_SOAP11_BINDING_URI, AR_URL).setIsDefault(true);

    Metadata spMetadata = new Metadata(spEntity, Metadata.MOCK_SP_KEY);
    Metadata smMetadata = new Metadata(smEntity, Metadata.SM_KEY);
    spMetadata.addPeerEntity(smEntity);
    smMetadata.addPeerEntity(spEntity);
  }

  public static String getMockSpUrl() {
    return MOCK_SP_URL;
  }
}
