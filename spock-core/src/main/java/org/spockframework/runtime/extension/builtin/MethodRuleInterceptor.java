/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spockframework.runtime.extension.builtin;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.FieldInfo;

import java.util.List;

public class MethodRuleInterceptor extends AbstractRuleInterceptor {
  MethodRuleInterceptor(List<FieldInfo> ruleFields) {
    super(ruleFields);
  }

  public void intercept(final IMethodInvocation invocation) throws Throwable {
    Statement statement = createBaseStatement(invocation);
    FrameworkMethod method = createFrameworkMethod(invocation);

    for (FieldInfo field : ruleFields) {
      MethodRule rule = (MethodRule) getRuleInstance(field, invocation.getInstance());
      statement = rule.apply(statement, method, invocation.getInstance());
    }

    statement.evaluate();
  }

  private FrameworkMethod createFrameworkMethod(final IMethodInvocation invocation) {
    return new FrameworkMethod(invocation.getIteration().getParent().getFeatureMethod().getReflection()) {
      @Override
      public String getName() {
        return invocation.getIteration().getDescription().getMethodName();
      }
    };
  }
}

