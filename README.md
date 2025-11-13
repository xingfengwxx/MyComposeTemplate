# MyApp - Android Jetpack Compose MVVM 项目

一个使用 Kotlin、Jetpack Compose 和 MVVM 架构构建的完整 Android 应用程序，使用 Hilt 进行依赖注入。

## 功能特性

- **登录界面**：带验证的邮箱/密码认证
- **首页**：显示用户列表，包含加载状态
- **详情页**：显示详细的用户信息
- **个人资料页**：显示已登录用户信息
- **设置页**：占位符设置界面
- **底部导航**：主界面之间的便捷导航

## 架构

- **MVVM**：Model-View-ViewModel 模式
- **Hilt**：依赖注入
- **Navigation Compose**：类型安全的导航
- **StateFlow**：响应式状态管理
- **Coroutines**：异步操作

## 项目结构

```
app/src/main/java/com/wangxingxing/mycomposeapp/
├── model/
│   └── User.kt
├── repository/
│   └── UserRepository.kt
├── viewmodel/
│   ├── LoginViewModel.kt
│   ├── HomeViewModel.kt
│   ├── DetailViewModel.kt
│   ├── ProfileViewModel.kt
│   └── SettingsViewModel.kt
├── ui/
│   ├── screens/
│   │   ├── LoginScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── DetailScreen.kt
│   │   ├── ProfileScreen.kt
│   │   ├── SettingsScreen.kt
│   │   └── MainScreen.kt
│   └── navigation/
│       └── Screen.kt
├── MainActivity.kt
└── MyApplication.kt
```

## 设置

1. 在 Android Studio 中打开项目
2. 同步 Gradle 文件
3. 在模拟器或物理设备上运行应用

## 要求

- Android Studio Hedgehog 或更高版本
- 最低 SDK：24
- 目标 SDK：34
- Kotlin 1.9.22

## 依赖项

- Jetpack Compose
- Navigation Compose
- Hilt 依赖注入
- Kotlin Coroutines
- Material 3

