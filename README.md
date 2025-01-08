# 物品管家 - Android物品管理应用

## 项目概述

物品管家是一款面向个人用户的物品管理Android应用程序。应用采用完全离线设计，所有数据存储在本地，
帮助用户便捷地管理个人物品信息，包括基本信息记录、图片管理、位置管理、数据统计等功能。

### 核心功能
- 物品信息管理（录入、编辑、删除）
  - 支持自定义物品类别
  - 灵活的属性定义
  - 标签系统
- 多角度图片记录
  - 支持拍照和从相册导入
  - 图片备注
  - 图片分类
- 分类管理系统
  - 多级分类
  - 自定义分类
  - 分类模板
- 存放位置管理
  - 位置树状结构
  - 房间/区域划分
  - 容器管理
- 多维度搜索
  - 全文检索
  - 标签筛选
  - 高级过滤
- 数据统计和可视化
  - 物品分布
  - 价值统计
  - 使用频率分析

## 技术架构

### 整体架构
项目采用Clean Architecture + MVVM架构模式，遵循单向数据流原则，确保代码的可维护性和可测试性。

```
├── 表现层 (Presentation Layer)
│   ├── UI (Compose)
│   └── ViewModel
├── 领域层 (Domain Layer)
│   ├── UseCase
│   ├── Repository Interface
│   └── Model
└── 数据层 (Data Layer)
    ├── Repository Implementation
    ├── Local Data Source
    └── File Storage
```

### 技术栈

#### UI层
- **Jetpack Compose**: 现代化声明式UI框架
- **Material Design 3**: 设计规范和组件
- **Navigation Compose**: 页面导航
- **Coil**: 图片加载和缓存
- **MPAndroidChart**: 数据可视化图表

#### 业务层
- **ViewModel**: 管理UI相关数据
- **Kotlin Coroutines & Flow**: 异步操作和响应式编程
- **Hilt**: 依赖注入
- **CameraX**: 相机功能实现

#### 数据层
- **Room**: 本地数据库
- **DataStore**: 轻量级数据存储
- **File Storage**: 本地文件存储

### 模块划分

#### 1. 物品管理模块
- 基本信息管理
  - 名称、描述、类别
  - 自定义属性
  - 标签管理
- 图片管理
  - 拍照/相册
  - 图片备注
  - 图片分类
- 状态管理
  - 物品状态跟踪
  - 状态变更记录

#### 2. 位置管理模块
- 区域管理
  - 房间/区域定义
  - 区域层级关系
- 容器管理
  - 容器定义
  - 容器层级关系
- 位置搜索
  - 快速定位
  - 路径导航

#### 3. 搜索与过滤模块
- 全文检索
  - 名称搜索
  - 描述搜索
  - 标签搜索
- 高级过滤
  - 多条件组合
  - 自定义过滤器
  - 过滤器保存

#### 4. 数据分析模块
- 物品统计
  - 数量统计
  - 分类分布
  - 状态分布
- 位置分析
  - 区域使用率
  - 空间利用率
- 使用分析
  - 使用频率
  - 维护记录

### 数据结构

核心数据实体：
```kotlin
data class Goods(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,                    // 物品名称
    val description: String? = null,     // 物品描述
    val brand: String? = null,           // 品牌
    val category: String,                // 物品类别
    val tags: List<String> = emptyList(),// 标签列表
    val attributes: Map<String, String> = emptyMap(), // 自定义属性
    val purchaseInfo: PurchaseInfo? = null,  // 购买信息
    val location: Location,              // 存放位置
    val photoUrls: List<String> = emptyList(), // 照片列表
    val status: GoodsStatus = GoodsStatus.NORMAL, // 物品状态
    val createTime: Date = Date(),
    val updateTime: Date = Date()
)

data class PurchaseInfo(
    val date: Date,
    val purchasePrice: Double,    // 购入价格
    val currentMarketPrice: Double? = null,  // 当前市场价格
    val channel: String          // 购买渠道
)

data class Location(
    val areaId: String,         // 区域ID
    val containerPath: String,   // 容器路径，例如："客厅/书柜/第二层"
    val detail: String? = null,  // 详细位置描述
    val photos: List<String> = emptyList()  // 位置提示图片列表
)

enum class GoodsStatus {
    NORMAL,     // 正常使用状态
    DISPOSED,   // 已处置（丢弃、送人等）
    DAMAGED     // 损坏状态
}
```

## 开发环境

- Android Studio Hedgehog | 2023.1.1
- Kotlin 1.9.0
- Minimum SDK: 21 (Android 5.0)
- Target SDK: 34 (Android 14)

## 依赖版本

主要依赖库版本：
- Compose BOM: 2023.10.01
- Room: 2.6.1
- Hilt: 2.48
- CameraX: 1.3.1
- Navigation Compose: 2.7.6
- Coil: 2.5.0

## 项目特点

1. **完全离线设计**
   - 所有数据本地存储
   - 无需网络连接
   - 保护用户隐私

2. **现代化UI**
   - 采用Jetpack Compose
   - Material Design 3设计规范
   - 流畅的动画和交互

3. **响应式架构**
   - 采用Kotlin Flow
   - 单向数据流
   - MVVM架构模式

4. **高性能**
   - Room数据库优化
   - 图片加载优化
   - 异步操作处理

## 开发规范

1. **代码风格**
   - 遵循Kotlin官方编码规范
   - 使用ktlint进行代码格式化

2. **架构原则**
   - 单一职责原则
   - 依赖注入
   - 接口隔离

3. **测试策略**
   - 单元测试
   - UI测试
   - 集成测试

## 项目结构

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/myaiapplication/
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── dao/
│   │   │   │   │   ├── entity/
│   │   │   │   │   └── database/
│   │   │   │   └── repository/
│   │   │   ├── domain/
│   │   │   │   ├── repository/
│   │   │   │   ├── usecase/
│   │   │   │   └── model/
│   │   │   ├── ui/
│   │   │   │   ├── theme/
│   │   │   │   ├── components/
│   │   │   │   └── screens/
│   │   │   └── util/
│   │   └── res/
│   └── test/
└── build.gradle.kts 
```

## 开发计划

### 第一阶段：基础架构搭建
1. 项目初始化
   - 依赖配置
   - 基础架构搭建
   - 数据库设计

2. 核心功能开发
   - 藏品信息管理
   - 基础UI组件
   - 数据层实现

### 第二阶段：功能实现
1. 图片管理模块
   - 相机集成
   - 图片存储
   - 图片编辑

2. 位置管理模块
   - 位置记录
   - 布局图设计

3. 搜索模块
   - 搜索界面
   - 过滤功能
   - 结果展示

### 第三阶段：数据分析与优化
1. 数据统计
   - 数据可视化
   - 统计报表

2. 性能优化
   - 数据库优化
   - 内存优化
   - 启动优化

## UI设计规范

### 色彩系统
- 主色：#FF6200EE
- 次要色：#FF3700B3
- 背景色：#FFFFFFFF
- 表面色：#FFFFFFFF
- 错误色：#FFB00020

### 字体规范
- 标题大号：24sp
- 标题中号：20sp
- 标题小号：16sp
- 正文：14sp
- 说明文字：12sp

### 间距规范
- 大间距：24dp
- 中间距：16dp
- 小间距：8dp
- 最小间距：4dp

### 组件规范
1. 按钮
   - 主要按钮：圆角8dp，高度48dp
   - 次要按钮：圆角4dp，高度40dp
   - 文字按钮：无背景，高度36dp

2. 卡片
   - 圆角：12dp
   - 阴影：2dp
   - 内边距：16dp

3. 列表
   - 项目高度：72dp
   - 分割线：0.5dp
   - 缩进：16dp

## Git工作流

### 分支管理
- main：主分支，用于发布
- develop：开发分支，日常开发
- feature/*：功能分支
- bugfix/*：问题修复
- release/*：发布准备

### 提交规范
提交信息格式：
```
<type>(<scope>): <subject>

<body>

<footer>
```

类型（type）：
- feat：新功能
- fix：修复
- docs：文档
- style：格式
- refactor：重构
- test：测试
- chore：构建

### 代码审查
- 每个PR必须经过代码审查
- 遵循代码规范
- 确保测试通过
- 性能影响评估

## 测试计划

### 单元测试
- ViewModel测试
- UseCase测试
- Repository测试
- 工具类测试

### UI测试
- 页面导航测试
- 组件交互测试
- 输入验证测试

### 集成测试
- 数据流测试
- 模块间交互测试
- 性能测试

## 发布计划

### 内部测试版
- 功能完整性验证
- 性能指标达标
- 内存泄漏检测

### Beta版本
- 用户体验优化
- 界面流畅度优化
- 稳定性测试

### 正式版本
- 最终功能确认
- 性能指标达标
- 文档完善 