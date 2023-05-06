package kr.hqservice.menu.runnable

import kr.hqservice.menu.repository.impl.MenuRepositoryImpl

class MenuSaveRunnable(private val menuRepository: MenuRepositoryImpl) : Runnable {

    override fun run() {
        menuRepository.saveAll()
    }
}