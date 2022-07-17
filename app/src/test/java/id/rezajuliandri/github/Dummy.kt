package id.rezajuliandri.github

import id.rezajuliandri.core.domain.model.User

object Dummy {
    fun userData(): User = User(
        123122312,
        "rezajuliandri",
        "Reza Juliandri",
        "https://avatars3.githubusercontent.com/u/52709818?v=4",
        899,
        89,
        null,
        null,
        null,
        false,
    )
    fun listUser(): List<User> {
        val list = mutableListOf<User>()
        for (i in 0..10) {
            list.add(userData())
        }
        return list
    }

}