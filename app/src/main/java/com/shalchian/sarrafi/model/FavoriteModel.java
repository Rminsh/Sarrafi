
/*
 *     This file is part of Sarrafi.
 *
 *     Sarrafi is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Sarrafi is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Sarrafi.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.shalchian.sarrafi.model;

public class FavoriteModel {

  private String objName, name;

  public String getObjName() {
    return objName;
  }

  public String getName() {
    return name;
  }

  public FavoriteModel(String objName,String name) {
    this.objName = objName;
    this.name = name;
  }

  @Override
  public boolean equals(Object other) {
    if(this == other)
      return true;
    if(other == null)
      return false;
    if(getClass() != other.getClass())
      return false;

    FavoriteModel test = (FavoriteModel)other;
    return this.objName.equals(test.getObjName()) && this.name.equals(test.getName());
  }
}
