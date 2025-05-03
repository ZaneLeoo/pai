# 排行榜 API 文档

## 基础信息

所有 API 返回的数据格式遵循统一的 Response 模型：

```json
{
  "code": 0, // 状态码，0表示成功
  "message": "success", // 状态消息
  "data": [] // 实际数据，类型因API而异
}
```

## 1. 热门文章排行榜

### 请求信息

- **URL**: `/api/rank/articles`
- **方法**: GET
- **参数**:
  - `limit`: 整数，可选，默认值 10，表示返回的文章数量

### 返回数据格式

```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "article": {
        "id": 1,
        "title": "文章标题",
        "summary": "文章摘要",
        "content": "文章内容",
        "authorId": 100,
        "createTime": "2023-05-01T10:30:00",
        "updateTime": "2023-05-01T15:45:00"
      },
      "author": {
        "id": 100,
        "username": "张三",
        "avatar": "https://example.com/avatar.jpg",
        "profile": "用户简介"
      },
      "likeCount": 156,
      "commentCount": 42,
      "viewCount": 789
    }
    // 更多热门文章...
  ]
}
```

## 2. 热门标签排行榜

### 请求信息

- **URL**: `/api/rank/tags`
- **方法**: GET
- **参数**:
  - `limit`: 整数，可选，默认值 10，表示返回的标签数量

### 返回数据格式

```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "tag": {
        "id": 1,
        "name": "Java",
        "createTime": "2023-01-15T08:30:00",
        "updateTime": "2023-04-20T14:25:00"
      },
      "followCount": 256,
      "articleCount": 78
    }
    // 更多热门标签...
  ]
}
```

## 3. 活跃作者排行榜

### 请求信息

- **URL**: `/api/rank/authors`
- **方法**: GET
- **参数**:
  - `limit`: 整数，可选，默认值 10，表示返回的作者数量

### 返回数据格式

```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "author": {
        "id": 100,
        "username": "张三",
        "avatar": "https://example.com/avatar.jpg",
        "profile": "用户简介",
        "createTime": "2022-10-05T09:15:00"
      },
      "score": 385.0, // 活跃度分数
      "rank": 1 // 排名
    }
    // 更多活跃作者...
  ]
}
```

## 积分计算规则

活跃作者排行榜的积分来源于用户行为，计算规则如下：

- 发布文章：+10 分
- 评论文章：+3 分
- 点赞文章：+2 分
- 浏览文章：+1 分

## 注意事项

1. 所有排行榜 API 支持`limit`参数控制返回数量
2. 活跃作者的积分每天每种行为只计算一次
3. 积分数据存储在 Redis 中，重启服务后可能会重置

## 代码调用示例

### 获取热门文章排行榜

```java
// 前端AJAX调用示例
$.ajax({
  url: '/api/rank/articles',
  method: 'GET',
  data: { limit: 5 },
  success: function(response) {
    if (response.code === 0) {
      const hotArticles = response.data;
      // 处理热门文章数据
    }
  }
});

// 后端服务调用示例
List<HotArticleVO> hotArticles = rankService.getHotArticles(5);
```

### 获取热门标签排行榜

```java
// 前端AJAX调用示例
$.ajax({
  url: '/api/rank/tags',
  method: 'GET',
  data: { limit: 10 },
  success: function(response) {
    if (response.code === 0) {
      const hotTags = response.data;
      // 处理热门标签数据
    }
  }
});

// 后端服务调用示例
List<HotTagVO> hotTags = rankService.getHotTags(10);
```

### 获取活跃作者排行榜

```java
// 前端AJAX调用示例
$.ajax({
  url: '/api/rank/authors',
  method: 'GET',
  data: { limit: 10 },
  success: function(response) {
    if (response.code === 0) {
      const activeAuthors = response.data;
      // 处理活跃作者数据
    }
  }
});

// 后端服务调用示例
List<ActiveAuthorVO> activeAuthors = scoreService.getActiveAuthors(10);
```
