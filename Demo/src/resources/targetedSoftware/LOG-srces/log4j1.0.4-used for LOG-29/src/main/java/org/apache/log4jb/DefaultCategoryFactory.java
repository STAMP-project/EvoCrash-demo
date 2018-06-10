/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.APL file.  */

import org.apache.log4jb.spi.CategoryFactory;

class DefaultCategoryFactory implements CategoryFactory {
    
  DefaultCategoryFactory() {
  }    
    
  public
  Category makeNewCategoryInstance(String name) {
    return new Category(name);
  }    
}