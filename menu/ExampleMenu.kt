package net.lumira.example

import net.lumira.api.condition.*
import net.lumira.api.coroutine.ComplexPlugin
import net.lumira.api.item.item
import net.lumira.api.menu.*
import net.lumira.api.menu.file.FileMenuLoader
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MenuExamplePlugin : ComplexPlugin() {

    private lateinit var fileMenuLoader: FileMenuLoader

    override fun onEnable() {
        fileMenuLoader = FileMenuLoader(this)

        fileMenuLoader.load("main_menu")
        fileMenuLoader.load("shop")
        fileMenuLoader.load("profile")

        logger.info("Menu system loaded!")
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) return false

        when (command.name.lowercase()) {
            "codemenu" -> codeMenu(sender)
            "conditionmenu" -> conditionMenu(sender)
            "actionmenu" -> actionMenuExample(sender)
            "filemenu" -> fileMenuExample(sender, args.getOrNull(0) ?: "main_menu")
            "shopmenu" -> shopMenuExample(sender)
        }

        return true
    }

    private fun codeMenu(player: Player) {
        menyByRows("&6&lПростое меню", rows = 3) {
            sounds {
                click("BLOCK_NOTE_BLOCK_HAT", 1.0f, 1.0f)
                open("BLOCK_BEACON_ACTIVATE", 0.3f, 2.0f)
                close("BLOCK_BEACON_DEACTIVATE", 0.3f, 2.0f)
            }

            onOpenActions(
                TellAction(listOf("&aМеню открыто!"))
            )

            val borderItem = item(Material.GRAY_STAINED_GLASS_PANE) {
                name(" ")
            }
            border(borderItem)

            slot(13) {
                item = item(Material.DIAMOND) {
                    name("&bАлмаз")
                    lore {
                        +"&7Нажмите для получения"
                    }
                }

                onClick = { clicker, _ ->
                    clicker.inventory.addItem(item(Material.DIAMOND))
                    clicker.sendMessage("&aВы получили алмаз!".colorize())
                }

                keepOpen(true)
            }
        }.open(player)
    }


    private fun conditionMenu(player: Player) {
        menyByRows("&cМеню с условиями", rows = 3) {
            condition {
                setPermission("menu.vip")
                setOnFalseActions(listOf(
                    TellAction(listOf("&cУ вас нет VIP статуса!")),
                    SoundAction("BLOCK_NOTE_BLOCK_DIDGERIDOO", 1.0f, 0.5f)
                ))
            }

            sounds {
                click("BLOCK_NOTE_BLOCK_PLING", 1.0f, 2.0f)
                open("BLOCK_BEACON_ACTIVATE", 0.3f, 2.0f)
            }

            // Слот с условием на клик
            slot(11) {
                item = item(Material.EMERALD) {
                    name("&aДоступно с 10+ уровнем")
                    lore {
                        +"&7Требуется 10 уровень"
                        +"&7для использования"
                    }
                }

                // Условие на клик
                clickCondition {
                    setGreaterThan("{player_level}", "9")
                    setOnFalseMessage("&cТребуется 10+ уровень!")
                }

                actions(
                    TellAction(listOf("&aУспешно использовано!")),
                    GiveItemAction("EMERALD", 1)
                )

                keepOpen(true)
            }

            // Слот с условием на отображение
            slot(13) {
                item = item(Material.CLOCK) {
                    name("&eВременный предмет")
                    lore {
                        +"&7Виден только"
                        +"&7с 20 по 40 секунду"
                    }
                }

                // Условие на отображение
                viewCondition {
                    setJavaScript("{server_time_ss} >= 20 && {server_time_ss} <= 40")
                }

                // Проверять каждую секунду
                viewCheckInterval(20)

                onClick = { clicker, _ ->
                    clicker.sendMessage("&aВы кликнули!".colorize())
                }
            }

            // Слот с JavaScript условием
            slot(15) {
                item = item(Material.REDSTONE) {
                    name("&cВыживание")
                    lore {
                        +"&7Доступно только"
                        +"&7в режиме выживания"
                    }
                }

                clickCondition {
                    setJavaScript("{player_gamemode} == 'SURVIVAL'")
                    setOnFalseMessage("&cДоступно только в выживании!")
                }

                action(TellAction(listOf("&aВы в режиме выживания!")))
            }
        }.open(player)
    }

    private fun actionMenuExample(player: Player) {
        menyByRows("&eДействия", rows = 4) {
            sounds {
                click("BLOCK_NOTE_BLOCK_HAT")
            }

            // Сообщение
            slot(10) {
                item = item(Material.PAPER) {
                    name("&eСообщение")
                }

                actions(
                    TellAction(listOf(
                        "&aПривет, {player}!",
                        "&7Твой уровень: {player_level}"
                    ))
                )
            }

            // Команда от консоли
            slot(11) {
                item = item(Material.COMMAND_BLOCK) {
                    name("&6Команда консоли")
                }

                actions(
                    ConsoleCommandAction(listOf(
                        "give {player} diamond 1"
                    ))
                )
            }

            // Команда от игрока
            slot(12) {
                item = item(Material.COMMAND_BLOCK_MINECART) {
                    name("&bКоманда игрока")
                }

                actions(
                    PlayerCommandAction(listOf("help"))
                )
            }

            // Звук
            slot(13) {
                item = item(Material.NOTE_BLOCK) {
                    name("&dЗвук")
                }

                actions(
                    SoundAction("ENTITY_PLAYER_LEVELUP", 1.0f, 1.0f)
                )
            }

            // Выдать предмет
            slot(14) {
                item = item(Material.DIAMOND) {
                    name("&bПредмет")
                }

                actions(
                    GiveItemAction("DIAMOND", 5, "&bАлмазы", listOf("&7x5"))
                )
            }

            // Открыть другое меню
            slot(15) {
                item = item(Material.ENDER_PEARL) {
                    name("&5Другое меню")
                }

                actions(
                    OpenMenuAction("main_menu")
                )
            }

            // Broadcast
            slot(16) {
                item = item(Material.BELL) {
                    name("&6Оповещение")
                }

                actions(
                    BroadcastAction(listOf(
                        "&6[Сервер] &f{player} &7нажал на колокольчик!"
                    ))
                )
            }

            // Закрыть меню
            slot(22) {
                item = item(Material.BARRIER) {
                    name("&cЗакрыть")
                }

                actions(
                    CloseMenuAction()
                )
            }
        }.open(player)
    }

    private fun fileMenuExample(player: Player, menuName: String) {
        val menu = fileMenuLoader.getMenu(menuName)

        if (menu != null) {
            menu.open(player)
        } else {
            player.sendMessage("&cМеню не найдено: $menuName".colorize())
        }
    }

    private fun shopMenuExample(player: Player) {
        data class ShopItem(
            val material: Material,
            val name: String,
            val price: Int,
            val requiredLevel: Int
        )

        val items = listOf(
            ShopItem(Material.DIAMOND_SWORD, "Меч", 100, 5),
            ShopItem(Material.DIAMOND_PICKAXE, "Кирка", 150, 10),
            ShopItem(Material.DIAMOND_AXE, "Топор", 120, 8),
            ShopItem(Material.DIAMOND_SHOVEL, "Лопата", 80, 5),
            ShopItem(Material.DIAMOND_HOE, "Мотыга", 60, 3),
            ShopItem(Material.DIAMOND_HELMET, "Шлем", 90, 7),
            ShopItem(Material.DIAMOND_CHESTPLATE, "Нагрудник", 200, 15),
            ShopItem(Material.DIAMOND_LEGGINGS, "Штаны", 150, 12),
            ShopItem(Material.DIAMOND_BOOTS, "Ботинки", 100, 8)
        )

        paginationMenu<ShopItem>("&6&lМАГАЗИН", size = 54) {
            items(items)

            itemRenderer { shopItem ->
                item(shopItem.material) {
                    name("&e${shopItem.name}")
                    lore {
                        separator()
                        +"&7Цена: &6${shopItem.price}$"
                        +"&7Требуется: &a${shopItem.requiredLevel} ур"
                        empty()

                        if (player.level >= shopItem.requiredLevel) {
                            +"&a✔ Доступно"
                        } else {
                            +"&c✗ Недостаточно уровня"
                        }

                        separator()
                    }
                }
            }

            onItemClick { clicker, shopItem ->
                val levelCondition = LevelCondition(shopItem.requiredLevel)

                if (!levelCondition.test(clicker)) {
                    clicker.sendMessage("&cТребуется ${shopItem.requiredLevel}+ уровень!".colorize())
                    return@onItemClick
                }

                // TODO: Проверка баланса
                
                val giveAction = GiveItemAction(
                    material = shopItem.material.name,
                    amount = 1,
                    name = "&e${shopItem.name}",
                    lore = listOf("&7Куплено за ${shopItem.price}$")
                )

                giveAction.execute(clicker, Replacer.EMPTY)

                clicker.sendMessage("&aКуплен: ${shopItem.name}".colorize())
            }

            useStandardContentSlots()
            border(item(Material.BLACK_STAINED_GLASS_PANE) { name(" ") })

            previousButton(45)
            nextButton(53)
            pageInfo(49)

            decoration(4) {
                item = item(Material.GOLD_INGOT) {
                    name("&6Баланс")
                    lore("&e1,234$")
                }
            }
        }.open(player)
    }

    private fun autoUpdateMenuExample(player: Player) {
        menyByRows("&eСтатистика", rows = 3) {
            slot(13) {
                item = item(Material.NETHER_STAR) {
                    name("&eИнформация")
                    lore {
                        +"&7Игроков: &a${server.onlinePlayers.size}"
                        +"&7Время: &f${System.currentTimeMillis()}"
                    }
                }
            }

            autoUpdate(20) { menu ->
                menu.updateSlot(13, item(Material.NETHER_STAR) {
                    name("&eИнформация")
                    lore {
                        +"&7Игроков: &a${server.onlinePlayers.size}"
                        +"&7Время: &f${System.currentTimeMillis()}"
                    }
                })
            }
        }.open(player)
    }

    override fun onPluginDisable() {
        menuManager.shutdown()
    }
}
