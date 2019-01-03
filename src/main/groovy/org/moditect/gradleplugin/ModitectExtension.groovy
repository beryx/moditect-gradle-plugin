/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.moditect.gradleplugin

import groovy.transform.CompileStatic
import org.moditect.gradleplugin.add.AddDependenciesModuleInfoTask
import org.moditect.gradleplugin.add.AddMainModuleInfoTask
import org.moditect.gradleplugin.generate.GenerateModuleInfoTask
import org.moditect.gradleplugin.image.CreateRuntimeImageTask

import static org.gradle.util.ConfigureUtil.configure

@CompileStatic
class ModitectExtension {
    final AddMainModuleInfoTask addMainModuleInfoTask
    final AddDependenciesModuleInfoTask addDependenciesModuleInfoTask
    final GenerateModuleInfoTask generateModuleInfoTask
    final CreateRuntimeImageTask createRuntimeImageTask

    ModitectExtension(AddMainModuleInfoTask addMainModuleInfoTask,
                      AddDependenciesModuleInfoTask addDependenciesModuleInfoTask,
                      GenerateModuleInfoTask generateModuleInfoTask,
                      CreateRuntimeImageTask createRuntimeImageTask) {
        this.addMainModuleInfoTask = addMainModuleInfoTask
        this.addDependenciesModuleInfoTask = addDependenciesModuleInfoTask
        this.generateModuleInfoTask = generateModuleInfoTask
        this.createRuntimeImageTask = createRuntimeImageTask
    }

    void addMainModuleInfoData(Closure closure) {
        configure(closure, addMainModuleInfoTask)

    }

    void addDependenciesModuleInfoTask(Closure closure) {
        configure(closure, addDependenciesModuleInfoTask)

    }

    void generateModuleInfoTask(Closure closure) {
        configure(closure, generateModuleInfoTask)

    }

    void createRuntimeImageTask(Closure closure) {
        configure(closure, createRuntimeImageTask)

    }
}
