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
package org.moditect.gradleplugin.generate.model

import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.moditect.gradleplugin.ModitectPlugin
import org.moditect.gradleplugin.Util
import org.moditect.gradleplugin.common.ModuleId
import org.moditect.gradleplugin.common.ModuleInfoConfiguration

import static org.gradle.util.ConfigureUtil.configure

@CompileStatic
@ToString(includeNames = true)
class ModuleConfiguration {
    private static final Logger LOGGER = Logging.getLogger(ModuleConfiguration)

    static final String PRIMARY_CFG_PREFIX = 'moditectGenPrimary'

    private final Project project
    private final int index

    Dependency primaryDependency
    private Configuration primaryConfig

    final ModuleInfoConfiguration moduleInfo = new ModuleInfoConfiguration()

    final Set<Dependency> additionalDependencies = []

    ModuleConfiguration(Project project, int index, Closure closure) {
        this.project = project
        this.index = index

        LOGGER.info "Calling closure of $shortName"
        configure(closure, this)
    }

    Dependency artifact(Object dependencyNotation, Closure closure) {
        Dependency dependency = artifact(dependencyNotation)
        configure(closure, dependency)
        dependency
    }

    Dependency artifact(Object dependencyNotation) {
        LOGGER.info "Creating primaryDependency of $shortName from: $dependencyNotation"

        if(primaryConfig) throw new GradleException("Multiple artifact() calls in $shortName")

        def basicConfigName = PRIMARY_CFG_PREFIX + index
        this.primaryConfig = project.configurations.create(basicConfigName)

        def notation = Util.getAdjustedDependencyNotation(project, dependencyNotation)
        primaryDependency = project.dependencies.add(basicConfigName, notation)
        if(primaryDependency instanceof ModuleDependency) {
            ((ModuleDependency)primaryDependency).transitive = false
        }
        project.dependencies.add(ModitectPlugin.FULL_CONFIGURATION_NAME, notation)
    }

    File getInputJar() {
        if(!primaryConfig) throw new GradleException("No artifact declaration found in $shortName")
        def artifacts = primaryConfig.resolvedConfiguration.resolvedArtifacts
        LOGGER.info "artifacts of $primaryConfig.name: $artifacts"
        artifacts.find { artifact -> !Util.isEmptyJar(artifact.file) }.file
    }

    Dependency additionalDependency(Object dependencyNotation, Closure closure) {
        def dependency = additionalDependency(dependencyNotation)
        configure(closure, dependency)
        dependency
    }

    Dependency additionalDependency(Object dependencyNotation) {
        LOGGER.info "Creating additionalDependency of $shortName from: $dependencyNotation"
        def dep = project.dependencies.add(ModitectPlugin.FULL_CONFIGURATION_NAME, dependencyNotation)
        additionalDependencies.add(dep)
        dep
    }

    void moduleInfo(Closure configureClosure) {
        configure(configureClosure, moduleInfo)
    }

    String getShortName() {
        "module #$index"
    }

    Set<ModuleId> getOptionalDependencies() {
        // TODO
        [] as Set
    }
}
