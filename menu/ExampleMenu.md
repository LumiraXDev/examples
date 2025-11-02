# LumiraAPI Menu ref

## ⚡ Быстрый старт

### Инициализация

```kotlin
class MyPlugin : ComplexPlugin() {
    override fun onEnable() {
        val fileMenuLoader = FileMenuLoader(this)
        fileMenuLoader.load("main_menu")
    }
}
```

### Простое меню

```kotlin
menu("&eМеню", rows = 3) {
    slot(13) {
        item = item(Material.DIAMOND) {
            name("&bАлмаз")
        }

        onClick = { player, _ ->
            player.sendMessage("&aКлик!".colorize())
        }
    }
}.open(player)
```

---

## 🔨 Меню из кода

### Создание меню

```kotlin
val menu = menuByRows(title = "&6&lМеню", rows = 3) {
    // Настройки меню
}

// Или с размером в слотах
val menu = menu(title = "&6&lМеню", size = 27) {
    // ...
}
```

### Слоты

```kotlin
menuByRows("Меню", rows = 3) {
    // Один слот
    slot(13) {
        item = myItem
        onClick = { player, event -> /* ... */ }
    }

    // Несколько слотов
    slots(10, 11, 12) {
        item = borderItem
    }

    // Диапазон слотов
    slots(0..8) {
        item = borderItem
    }
}
```

### Условия на открытие

```kotlin
menuByRows("VIP Меню", rows = 3) {
    // Условие на открытие
    condition {
        setPermission("menu.vip")
        setOnFalseActions(listOf(
            TellAction(listOf("&cНет VIP статуса!")),
            SoundAction("BLOCK_NOTE_BLOCK_DIDGERIDOO", 1.0f, 0.5f)
        ))
    }

    // Можно добавить несколько условий
    condition {
        setGreaterThan("{player_level}", "10")
        setOnFalseMessage("&cТребуется 10+ уровень!")
    }
}
```

### Условия на слоты

```kotlin
slot(13) {
    item = myItem

    // Условие на клик
    clickCondition {
        setPermission("menu.item.use")
        setOnFalseMessage("&cНет прав!")
    }

    // Условие на отображение
    viewCondition {
        setJavaScript("{player_level} >= 10")
    }

    // Интервал проверки отображения (в тиках)
    viewCheckInterval(20) // Каждую секунду

    onClick = { player, _ ->
        player.sendMessage("&aИспользовано!".colorize())
    }
}
```

### Действия слотов

```kotlin
slot(13) {
    item = myItem

    // Добавить действия
    actions(
        TellAction(listOf("&aСообщение!")),
        GiveItemAction("DIAMOND", 1),
        SoundAction("ENTITY_PLAYER_LEVELUP", 1.0f, 1.0f)
    )

    // Или по одному
    action(TellAction(listOf("&aТекст")))
    action(CloseMenuAction())

    // Не закрывать меню после клика
    keepOpen(true)

    // Кастомный звук для слота
    sound("BLOCK_NOTE_BLOCK_PLING", 1.0f, 2.0f)
}
```

### Звуки меню

```kotlin
menuByRows("Меню", rows = 3) {
    sounds {
        // Звук при клике
        click("BLOCK_NOTE_BLOCK_HAT", 1.0f, 1.0f)

        // Звук при открытии
        open("BLOCK_BEACON_ACTIVATE", 0.3f, 2.0f)

        // Звук при закрытии
        close("BLOCK_BEACON_DEACTIVATE", 0.3f, 2.0f)

        // Звук при переходе между меню
        transition("BLOCK_END_PORTAL_FRAME_FILL", 0.3f, 1.2f)
    }
}
```

### События меню

```kotlin
menuByRows("Меню", rows = 3) {
    // При открытии
    onOpen { player ->
        player.sendMessage("&aМеню открыто!".colorize())
    }

    // При открытии (actions)
    onOpenActions(
        TellAction(listOf("&aДобро пожаловать!")),
        SoundAction("BLOCK_BELL_USE", 1.0f, 1.0f)
    )

    // При закрытии
    onClose { player ->
        player.sendMessage("&cМеню закрыто".colorize())
    }

    // При закрытии (actions)
    onCloseActions(
        TellAction(listOf("&cДо свидания!"))
    )

    // При переходе в меню (из другого меню)
    onTransitionIn { player ->
        player.sendMessage("&eВы перешли в меню".colorize())
    }

    // При переходе из меню (в другое меню)
    onTransitionOut { player ->
        player.sendMessage("&eВы покидаете меню".colorize())
    }
}
```

### Автообновление

```kotlin
menuByRows("Статистика", rows = 3) {
    slot(13) {
        item = statsItem
    }

    // Обновлять каждую секунду (20 тиков)
    autoUpdate(20) { menu ->
        menu.updateSlot(13, item(Material.NETHER_STAR) {
            name("&eИгроков: &a${Bukkit.getOnlinePlayers().size}")
        })
    }
}
```

### Утилиты

```kotlin
menuByRows("Меню", rows = 3) {
    val borderItem = item(Material.GRAY_STAINED_GLASS_PANE) { name(" ") }

    // Рамка
    border(borderItem)

    // Заполнить всё меню
    fill(borderItem)

    // Заполнить пустые слоты
    fillEmpty(borderItem)
}
```

---

## 📁 Файловые меню

### Формат YAML

```yaml
# Включить меню
enable: true

# Команды для открытия
commands:
  - 'menu'
  - 'mainmenu'

# Заголовок
title:
  text: '&e&lГлавное меню для {player}'

# Количество строк (1-6)
rows: 3

# Звуки
sounds:
  click:
    name: BLOCK_NOTE_BLOCK_HAT
    volume: 1.0
    pitch: 1.0
  open:
    name: BLOCK_BEACON_ACTIVATE
    volume: 0.3
    pitch: 2.0
  close:
    name: BLOCK_BEACON_DEACTIVATE
    volume: 0.3
    pitch: 2.0
  transition:
    name: BLOCK_END_PORTAL_FRAME_FILL
    volume: 0.3
    pitch: 1.2

# Условия на открытие меню
conditions:
  permission-check:
    permission: 'menu.use'
    message: '&cУ вас нет прав!'

  level-check:
    javascript: '{player_level} >= 5'
    message: '&cТребуется 5+ уровень!'

# Действия при событиях
actions:
  open:
    - tell:
        - '&aМеню открыто!'
  close:
    - tell:
        - '&cМеню закрыто'

# Иконки
icons:
  profile:
    position:
      x: 5
      y: 2

    material: PLAYER_HEAD
    name: '&eПрофиль'
    lore:
      - '&7Ваш профиль'
      - '&7Уровень: {player_level}'

    # Условие на клик
    condition:
      permission: 'menu.profile'
      message: '&cНет прав!'

    # Условие на отображение
    condition-view:
      javascript: '{player_level} >= 10'

    # Интервал проверки отображения
    view-check-rounded: 1s

    # Не закрывать меню
    keep-open: true

    # Кастомный звук
    sound: 'ENTITY_PLAYER_LEVELUP, 1.0, 1.0'

    # Действия при клике
    actions:
      - tell:
          - '&aОткрытие профиля...'
      - open: 'profile_menu'

  shop:
    position:
      x: 7
      y: 2

    material: EMERALD
    name: '&aМагазин'

    actions:
      - tell:
          - '&aОткрытие магазина...'
      - command: 'shop open {player}'
```

### Загрузка файлового меню

```kotlin
class MyPlugin : ComplexPlugin() {
    private lateinit var fileMenuLoader: FileMenuLoader

    override fun onEnable() {
        fileMenuLoader = FileMenuLoader(this)
        
        fileMenuLoader.load("main_menu")
        fileMenuLoader.load("shop")
        
        val menu = fileMenuLoader.getMenu("main_menu")
        menu?.open(player)
    }
}
```

### Команды меню

Команды регистрируются автоматически из секции `commands`:

```yaml
commands:
  - 'menu'
  - 'mainmenu'
  - 'mm'
```

После загрузки эти команды будут открывать меню.

---

## 🎯 Условия

### Типы условий

```yaml
# Permission
permission: 'menu.vip'
message: '&cНет прав!'

# JavaScript
javascript: '{player_level} >= 10'
message: '&cТребуется 10+ уровень!'

# Сравнение
left: '{player_world}'
right: 'world'
operator: '=='
message: '&cДоступно только в основном мире'
```

### В коде

```kotlin
condition {
    setPermission("menu.vip")
    setOnFalseMessage("&cНет VIP!")
}

condition {
    setJavaScript("{player_level} >= 10")
    setOnFalseActions(listOf(
        TellAction(listOf("&cТребуется 10+ уровень!")),
        SoundAction("BLOCK_NOTE_BLOCK_DIDGERIDOO", 1.0f, 0.5f)
    ))
}

condition {
    setEqual("{player_gamemode}", "SURVIVAL")
    setOnFalseMessage("&cТолько в режиме выживания!")
}
```

---

## 🎬 Действия

### Доступные действия

```yaml
# Сообщение
- tell:
    - '&aТекст'
    - '&7Вторая строка'

# Команда от консоли
- command: 'give {player} diamond 1'
- console: 'tp {player} 0 100 0'

# Команда от игрока
- player: 'help'

# Звук
- sound: 'ENTITY_PLAYER_LEVELUP, 1.0, 1.0'

# Закрыть меню
- close: true

# Открыть другое меню
- open: 'shop_menu'

# Broadcast
- broadcast:
    - '&6{player} &7открыл меню!'

# Выдать предмет
- give:
    material: DIAMOND
    amount: 5
    name: '&bАлмазы'
    lore:
      - '&7Получено из меню'
```

### В коде

```kotlin
slot(13) {
    item = myItem

    actions(
        TellAction(listOf("&aСообщение")),
        ConsoleCommandAction(listOf("give {player} diamond 1")),
        PlayerCommandAction(listOf("help")),
        SoundAction("ENTITY_PLAYER_LEVELUP", 1.0f, 1.0f),
        GiveItemAction("DIAMOND", 5, "&bАлмазы"),
        OpenMenuAction("shop"),
        CloseMenuAction()
    )
}
```

---

## 🔊 Звуки

### В файле

```yaml
sounds:
  click:
    name: BLOCK_NOTE_BLOCK_HAT
    volume: 1.0
    pitch: 1.0
  open:
    name: BLOCK_BEACON_ACTIVATE
    volume: 0.3
    pitch: 2.0

# Кастомный звук для иконки
icons:
  item:
    sound: 'ENTITY_PLAYER_LEVELUP, 1.0, 1.0'
    # Или отключить звук
    sound: false
```

### В коде

```kotlin
sounds {
    click("BLOCK_NOTE_BLOCK_HAT", 1.0f, 1.0f)
    open("BLOCK_BEACON_ACTIVATE", 0.3f, 2.0f)
    close("BLOCK_BEACON_DEACTIVATE", 0.3f, 2.0f)
    transition("BLOCK_END_PORTAL_FRAME_FILL", 0.3f, 1.2f)
}

// Кастомный звук для слота
slot(13) {
    sound("ENTITY_PLAYER_LEVELUP", 1.0f, 1.0f)
}
```

---

## 💡 Примеры

### 1. Простое меню

```kotlin
menu("&eМеню", rows = 3) {
    sounds {
        click("BLOCK_NOTE_BLOCK_HAT")
        open("BLOCK_BEACON_ACTIVATE", 0.3f, 2.0f)
    }

    slot(13) {
        item = item(Material.DIAMOND) {
            name("&bАлмаз")
        }

        onClick = { player, _ ->
            player.inventory.addItem(item(Material.DIAMOND))
        }

        keepOpen(true)
    }
}.open(player)
```

### 2. Меню с условиями

```kotlin
menu("&cVIP Меню", rows = 3) {
    condition {
        setPermission("menu.vip")
        setOnFalseActions(listOf(
            TellAction(listOf("&cНет VIP!")),
            SoundAction("BLOCK_NOTE_BLOCK_DIDGERIDOO", 1.0f, 0.5f)
        ))
    }

    slot(13) {
        item = item(Material.EMERALD) {
            name("&aVIP Награда")
        }

        clickCondition {
            setGreaterThan("{player_level}", "9")
            setOnFalseMessage("&cТребуется 10+ уровень!")
        }

        actions(
            GiveItemAction("EMERALD", 1),
            TellAction(listOf("&aНаграда получена!"))
        )
    }
}.open(player)
```

### 3. Динамические слоты

```kotlin
menu("&eДинамика", rows = 3) {
    slot(13) {
        item = item(Material.CLOCK) {
            name("&eВременный предмет")
        }

        // Видно только с 20 по 40 секунду
        viewCondition {
            setJavaScript("{server_time_ss} >= 20 && {server_time_ss} <= 40")
        }

        viewCheckInterval(20)
    }
}.open(player)
```

### 4. Магазин

```kotlin
data class ShopItem(val material: Material, val name: String, val price: Int, val level: Int)

val items = listOf(
    ShopItem(Material.DIAMOND_SWORD, "Меч", 100, 5),
    ShopItem(Material.DIAMOND_PICKAXE, "Кирка", 150, 10)
)

paginationMenu<ShopItem>("&6&lМАГАЗИН", size = 54) {
    items(items)

    itemRenderer { shopItem ->
        item(shopItem.material) {
            name("&e${shopItem.name}")
            lore {
                +"&7Цена: &6${shopItem.price}$"
                +"&7Уровень: &a${shopItem.level}"
            }
        }
    }

    onItemClick { player, shopItem ->
        val condition = LevelCondition(shopItem.level)
        if (!condition.test(player)) {
            player.sendMessage("&cТребуется ${shopItem.level}+ уровень!".colorize())
            return@onItemClick
        }
        
        GiveItemAction(shopItem.material.name, 1).execute(player, Replacer.EMPTY)
    }

    useStandardContentSlots()
    border(item(Material.BLACK_STAINED_GLASS_PANE) { name(" ") })

    previousButton(45)
    nextButton(53)
    pageInfo(49)
}.open(player)
```

### 5. Файловое меню

`menus/main.yml`:
```yaml
enable: true
commands:
  - 'menu'

title:
  text: '&e&lГлавное меню'

rows: 3

sounds:
  click:
    name: BLOCK_NOTE_BLOCK_HAT
    volume: 1.0
    pitch: 1.0

icons:
  profile:
    position:
      x: 5
      y: 2
    material: PLAYER_HEAD
    name: '&eПрофиль'
    actions:
      - open: 'profile'
```

Код:
```kotlin
val loader = FileMenuLoader(plugin)
loader.load("main")

val menu = loader.getMenu("main")
menu?.open(player)
```
