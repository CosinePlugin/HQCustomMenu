package kr.hqservice.menu.repository

interface MenuRepository<K, V> {

    fun reload()

    fun reload(name: K)

    fun createMenu(name: K)

    fun deleteMenu(name: K)

    fun isCreated(name: K): Boolean

    fun getMenu(name: K): V?

    fun getMenus(): List<K>

    fun getMenuMap(): Map<K, V>
}