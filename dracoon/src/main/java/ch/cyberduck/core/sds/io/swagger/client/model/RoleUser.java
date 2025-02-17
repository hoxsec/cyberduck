/*
 * DRACOON API
 * REST Web Services for DRACOON<br><br>This page provides an overview of all available and documented DRACOON APIs, which are grouped by tags.<br>Each tag provides a collection of APIs that are intended for a specific area of the DRACOON.<br><br><a title='Developer Information' href='https://developer.dracoon.com'>Developer Information</a>&emsp;&emsp;<a title='Get SDKs on GitHub' href='https://github.com/dracoon'>Get SDKs on GitHub</a><br><br><a title='Terms of service' href='https://www.dracoon.com/terms/general-terms-and-conditions/'>Terms of service</a>
 *
 * OpenAPI spec version: 4.30.0-beta.4
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package ch.cyberduck.core.sds.io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import ch.cyberduck.core.sds.io.swagger.client.model.UserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * User information
 */
@Schema(description = "User information")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-08-16T11:28:10.116221+02:00[Europe/Zurich]")
public class RoleUser {
  @JsonProperty("userInfo")
  private UserInfo userInfo = null;

  @JsonProperty("isMember")
  private Boolean isMember = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("displayName")
  private String displayName = null;

  public RoleUser userInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
    return this;
  }

   /**
   * Get userInfo
   * @return userInfo
  **/
  @Schema(required = true, description = "")
  public UserInfo getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }

  public RoleUser isMember(Boolean isMember) {
    this.isMember = isMember;
    return this;
  }

   /**
   * Is user member of the role
   * @return isMember
  **/
  @Schema(required = true, description = "Is user member of the role")
  public Boolean isIsMember() {
    return isMember;
  }

  public void setIsMember(Boolean isMember) {
    this.isMember = isMember;
  }

  public RoleUser id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * &amp;#128679; Deprecated since v4.11.0  Unique identifier for the user  use &#x60;id&#x60; from &#x60;UserInfo&#x60; instead
   * @return id
  **/
  @Schema(required = true, description = "&#128679; Deprecated since v4.11.0  Unique identifier for the user  use `id` from `UserInfo` instead")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RoleUser displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

   /**
   * &amp;#128679; Deprecated since v4.11.0  Display name  use information from &#x60;UserInfo&#x60; instead to combine a display name
   * @return displayName
  **/
  @Schema(required = true, description = "&#128679; Deprecated since v4.11.0  Display name  use information from `UserInfo` instead to combine a display name")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleUser roleUser = (RoleUser) o;
    return Objects.equals(this.userInfo, roleUser.userInfo) &&
        Objects.equals(this.isMember, roleUser.isMember) &&
        Objects.equals(this.id, roleUser.id) &&
        Objects.equals(this.displayName, roleUser.displayName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userInfo, isMember, id, displayName);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleUser {\n");
    
    sb.append("    userInfo: ").append(toIndentedString(userInfo)).append("\n");
    sb.append("    isMember: ").append(toIndentedString(isMember)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
