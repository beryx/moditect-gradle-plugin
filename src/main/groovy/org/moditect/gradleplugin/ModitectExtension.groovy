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
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.moditect.gradleplugin.add.AddDependenciesModuleInfoTask
import org.moditect.gradleplugin.add.AddMainModuleInfoTask
import org.moditect.gradleplugin.aether.DependencyResolver
import org.moditect.gradleplugin.generate.GenerateModuleInfoTask
import org.moditect.gradleplugin.image.CreateRuntimeImageTask

import static org.gradle.util.ConfigureUtil.configure

@CompileStatic
class ModitectExtension {
    private final Project project

    final AddMainModuleInfoTask addMainModuleInfoTask
    final AddDependenciesModuleInfoTask addDependenciesModuleInfoTask
    final GenerateModuleInfoTask generateModuleInfoTask
    final CreateRuntimeImageTask createRuntimeImageTask

    final DependencyResolver dependencyResolver

    ModitectExtension(Project project,
                      AddMainModuleInfoTask addMainModuleInfoTask,
                      AddDependenciesModuleInfoTask addDependenciesModuleInfoTask,
                      GenerateModuleInfoTask generateModuleInfoTask,
                      CreateRuntimeImageTask createRuntimeImageTask) {
        this.addMainModuleInfoTask = addMainModuleInfoTask
        this.addDependenciesModuleInfoTask = addDependenciesModuleInfoTask
        this.generateModuleInfoTask = generateModuleInfoTask
        this.createRuntimeImageTask = createRuntimeImageTask

        this.dependencyResolver = new DependencyResolver(project)

        project.afterEvaluate {
            addDependenciesModuleInfoTask.modules.get().moduleConfigurations.each {cfg ->
                cfg.updateFullConfiguration()
            }
            def cfg = project.configurations.getByName(ModitectPlugin.FULL_CONFIGURATION_NAME)
            cfg.extendsFrom(project.configurations.getByName(JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME))
            cfg.canBeResolved = true
            cfg.resolve()
        }
    }

    void addMainModuleInfoData(Closure closure) {
        configure(closure, addMainModuleInfoTask)
    }

    void addDependenciesModuleInfo(Closure closure) {
        configure(closure, addDependenciesModuleInfoTask)
    }

    void generateModuleInfo(Closure closure) {
        configure(closure, generateModuleInfoTask)
        // TODO
//        generateModuleInfoTask.modules.get().moduleConfigurations.each {cfg ->
//            cfg.updateFullConfiguration()
//        }
    }

    void createRuntimeImage(Closure closure) {
        configure(closure, createRuntimeImageTask)
    }
}
