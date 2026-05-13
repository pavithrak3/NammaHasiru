package com.example.nammahasiru

object PlantImageMapper {
    fun getWikimediaImageUrl(plantName: String): String {
        val encoded = plantName.trim().replace(" ", "_")
        return "https://en.wikipedia.org/w/api.php?action=query&titles=$encoded&prop=pageimages&format=json&pithumbsize=300"
    }

    // Fallback preset images for common plants
    private val plantImages = mapOf(
        "tulsi" to "https://upload.wikimedia.org/wikipedia/commons/thumb/8/88/Tulsi_in_a_pot.jpg/400px-Tulsi_in_a_pot.jpg",
        "neem" to "https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Neem_A_Azadirachta_indica.jpg/400px-Neem_A_Azadirachta_indica.jpg",
        "rose" to "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Rosa_rubiginosa_1.jpg/400px-Rosa_rubiginosa_1.jpg",
        "banana" to "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Banana-Trompo.jpg/400px-Banana-Trompo.jpg",
        "lily" to "https://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Lilium_flowers.jpg/400px-Lilium_flowers.jpg",
        "sunflower" to "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/A_sunflower.jpg/400px-A_sunflower.jpg",
        "tulips" to "https://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Sunflower_from_Silesia2.jpg/400px-Sunflower_from_Silesia2.jpg",
        "money plant" to "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Epipremnum_aureum_31082012.jpg/400px-Epipremnum_aureum_31082012.jpg",
        "ginger" to "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Ginger_roots.jpg/400px-Ginger_roots.jpg",
        "mango" to "https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Hapus_Mango.jpg/400px-Hapus_Mango.jpg",
        "hibiscus" to "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b8/Hibiscus_rosa-sinensis.jpg/400px-Hibiscus_rosa-sinensis.jpg",
        "lotus" to "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b8/Lotus_flower_%28978659%29.jpg/400px-Lotus_flower_%28978659%29.jpg",
        "jasmine" to "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/Jasminum_officinale.jpg/400px-Jasminum_officinale.jpg",
        "aloe vera" to "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Aloe_vera_flower_inset.png/400px-Aloe_vera_flower_inset.png",
        "aleovra" to "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Aloe_vera_flower_inset.png/400px-Aloe_vera_flower_inset.png",
        "coconut" to "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f2/Coconut_on_tree_2.jpg/400px-Coconut_on_tree_2.jpg",
        "banyan" to "https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Banyan_tree.jpg/400px-Banyan_tree.jpg",
        "peepal" to "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9b/Ficus_religiosa_bo.jpg/400px-Ficus_religiosa_bo.jpg",
        "bamboo" to "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Bamboo_in_Huy.jpg/400px-Bamboo_in_Huy.jpg",
        "papaya" to "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Papaya_cross_section_and_whole.jpg/400px-Papaya_cross_section_and_whole.jpg",
        "guava" to "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8c/Guava_ID.jpg/400px-Guava_ID.jpg",
        "tomato" to "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Tomato_je.jpg/400px-Tomato_je.jpg",
        "marigold" to "https://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Simple_marigold.jpg/400px-Simple_marigold.jpg",
        "lavender" to "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Lavandula_angustifolia.jpg/400px-Lavandula_angustifolia.jpg",
        "cactus" to "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Ferocactus_cylindraceus.jpg/400px-Ferocactus_cylindraceus.jpg",
        "mint" to "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Mintleaf_edit.jpg/400px-Mintleaf_edit.jpg",
        "lemon" to "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b4/Lemon_drop.jpg/400px-Lemon_drop.jpg"
    )

    fun getImageUrl(plantName: String): String? {
        return plantImages[plantName.lowercase().trim()]
    }
}