/*
 * Copyright 2009-2014 Marcelo Guimarães
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.devnull.trugger.element;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class OtherTestObject {
  
  private String name;
  private String lastName;
  
  private StringBuilder nickName;
  
  private double weight;
  
  public String getNickName() {
    return nickName == null ? null : nickName.toString();
  }
  
  public void setNickName(String nickName) {
    if (nickName != null) {
      this.nickName = new StringBuilder(nickName);
    } else {
      this.nickName = null;
    }
  }
  
  public double getWeight() {
    return weight;
  }
  
  public void setWeight(double weight) {
    this.weight = weight;
  }
  
  public String getName() {
    return name;
  }
  
  public String getLastName() {
    return lastName;
  }
  
}
