# API 文档

## 基础信息

### Base URL

```
http://your-server/
```

### 通用返回格式

所有 API 都使用 `Response<T>` 封装返回数据：

```json
{
  "code": 0,       // 0表示成功，非0表示错误
  "message": "操作成功", // 提示信息
  "data": {...}    // 实际返回数据，根据接口不同而变化
}
```

## 文章管理 (Article)

### 获取文章列表

```
GET /admin/article/list
```

**分页参数**:

- `page`: 页码，默认为 1
- `size`: 每页大小，默认为 10

**示例**: `/admin/article/list?page=1&size=15`

**返回数据**:

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "文章标题",
        "content": "文章内容",
        "authorId": 100,
        "... 其他字段": "..."
      }
      // 更多文章记录
    ],
    "total": 150,
    "size": 15,
    "current": 1,
    "pages": 10
  }
}
```

### 更新文章

```
POST /admin/article/update
```

**请求体**:

```json
{
  "id": 1, // 必须，文章ID
  "title": "标题", // 可选，要更新的字段
  "content": "内容" // 可选，要更新的字段
  // 其他字段...
}
```

**返回数据**:

```json
{
  "code": 0,
  "message": "更新成功",
  "data": "更新成功"
}
```

### 删除文章

```
POST /admin/article/delete
```

**请求体**:

```json
[1, 2, 3] // 文章ID列表，可以单个或批量删除
```

**返回数据**:

```json
{
  "code": 0,
  "message": "删除成功",
  "data": "删除成功"
}
```

## 用户管理 (User)

### 获取用户列表

```
GET /admin/user/list
```

**分页参数**:

- `page`: 页码，默认为 1
- `size`: 每页大小，默认为 10

**示例**: `/admin/user/list?page=1&size=15`

**返回数据**:

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "用户名",
        "email": "邮箱",
        "... 其他字段": "..."
      }
      // 更多用户记录
    ],
    "total": 150,
    "size": 15,
    "current": 1,
    "pages": 10
  }
}
```

### 添加用户

```
POST /admin/user/add
```

**请求体**:

```json
{
  "username": "用户名", // 必须
  "password": "密码", // 必须
  "email": "邮箱" // 可选
  // 其他字段...
}
```

**返回数据**:

```json
{
  "code": 0,
  "message": "添加成功",
  "data": "添加成功"
}
```

### 更新用户

```
POST /admin/user/update
```

**请求体**:

```json
{
  "id": 1, // 必须，用户ID
  "username": "用户名", // 可选，要更新的字段
  "password": "密码" // 可选，要更新的字段
  // 其他字段...
}
```

**返回数据**:

```json
{
  "code": 0,
  "message": "更新成功",
  "data": "更新成功"
}
```

### 删除用户

```
POST /admin/user/delete
```

**请求体**:

```json
[1, 2, 3] // 用户ID列表，可以单个或批量删除
```

**返回数据**:

```json
{
  "code": 0,
  "message": "删除成功",
  "data": "删除成功"
}
```

## 标签管理 (Tag)

### 获取标签列表

```
GET /admin/tag/list
```

**分页参数**:

- `page`: 页码，默认为 1
- `size`: 每页大小，默认为 10

**示例**: `/admin/tag/list?page=1&size=15`

**返回数据**:

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "标签名称",
        "... 其他字段": "..."
      }
      // 更多标签记录
    ],
    "total": 150,
    "size": 15,
    "current": 1,
    "pages": 10
  }
}
```

### 添加标签

```
POST /admin/tag/add
```

**请求体**:

```json
{
  "name": "标签名称" // 必须
  // 其他字段...
}
```

**返回数据**:

```json
{
  "code": 0,
  "message": "添加成功",
  "data": "添加成功"
}
```

### 更新标签

```
POST /admin/tag/update
```

**请求体**:

```json
{
  "id": 1, // 必须，标签ID
  "name": "标签名称" // 可选，要更新的字段
  // 其他字段...
}
```

**返回数据**:

```json
{
  "code": 0,
  "message": "更新成功",
  "data": "更新成功"
}
```

### 删除标签

```
POST /admin/tag/delete
```

**请求体**:

```json
[1, 2, 3] // 标签ID列表，可以单个或批量删除
```

**返回数据**:

```json
{
  "code": 0,
  "message": "删除成功",
  "data": "删除成功"
}
```

## 注意事项

1. 所有列表接口都支持分页，使用 `page` 和 `size` 参数
2. 当前接口不支持搜索参数，如需添加，建议使用 `keyword` 或 `query` 参数
3. 所有更新和删除操作都需要提供 ID
4. 删除操作支持批量处理，接受 ID 数组
