package com.github.ybroeker.pmdidea.services

import com.intellij.openapi.project.Project
import com.github.ybroeker.pmdidea.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
