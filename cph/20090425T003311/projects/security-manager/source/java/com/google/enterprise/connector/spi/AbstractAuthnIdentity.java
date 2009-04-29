// Copyright (C) 2009 Google Inc.
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

package com.google.enterprise.connector.spi;


import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * A base for implementing SecAuthnIdentity classes.
 */
public abstract class AbstractAuthnIdentity implements SecAuthnIdentity {

  private VerificationStatus status;

  protected AbstractAuthnIdentity() {
    status = VerificationStatus.TBD;
  }

  /**
   * Get the session for this identity.
   *
   * @return The identity's session object.
   */
  public abstract HttpSession getSession();

  /**
   * Get the cookies associated with this identity.
   *
   * These are the cookies received from the IdP.  This collection may be modified.
   *
   * @return The identity's cookies.
   */
  public abstract Collection<Cookie> getCookies();

  /**
   * Associate a cookie with this identity.
   * If the cookie's name is the same as a previously associated cookie, the
   * implementation is allowed to overwrite the previous cookie, so don't count on there
   * being multiple cookies with the same name.
   * @param c The cookie to associate.
   */
  public void addCookie(Cookie c) {
    getCookies().add(c);
  }

  /**
   * Get an associated cookie by name.
   * @param name The name of the cookie to return.
   * @return The associated cookie, or null if no such cookie.
   */
  public Cookie getCookieNamed(String name) {
    for (Cookie c : getCookies()) {
      if (c.getName().equals(name)) {
        return c;
      }
    }
    return null;
  }

  /**
   * Get the verification status for this identity.
   * @return The identity's verification status.
   */
  public VerificationStatus getVerificationStatus() {
    return status;
  }

  /**
   * Set the verification status for this identity.
   * @param status The new verification status.
   */
  public void setVerificationStatus(VerificationStatus status) {
    this.status = status;
  }

  /**
   * Return a login URL for identity types that require one.
   * @return The login URL, or null if no such.
   */
  public abstract String getSampleUrl();

  /**
   * Set the identity's username.
   *
   * @param username The new username, must not be null.
   */
  public abstract void setUsername(String username);
}
