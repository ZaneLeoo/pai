# RabbitMQ 消息通知系统使用示例

本文档提供了如何使用 RabbitMQ 消息系统发送积分和通知消息的示例代码。

## 1. 积分消息发送

### 1.1 发布文章时增加积分

```java
// 首先注入MQUtil
@Resource
private MQUtil mqUtil;

// 在发布文章的方法中发送积分消息
public void publishArticle(Article article) {
    // 保存文章
    save(article);

    // 发送积分消息
    mqUtil.sendMessage(
        RabbitMQConfig.USER_ACTION_EXCHANGE,
        "score.publish",
        ScoreMessage.builder()
            .userId(article.getAuthorId())
            .score(ScoreConstants.ScoreRule.PUBLISH_ARTICLE)
            .action("publish")
            .resourceId(article.getId())
            .build()
    );
}
```

### 1.2 浏览文章时增加积分

```java
public void viewArticle(Long articleId, Long userId) {
    // 发送积分消息
    mqUtil.sendMessage(
        RabbitMQConfig.USER_ACTION_EXCHANGE,
        "score.view",
        ScoreMessage.builder()
            .userId(userId)
            .score(ScoreConstants.ScoreRule.VIEW_ARTICLE)
            .action("view")
            .resourceId(articleId)
            .build()
    );
}
```

### 1.3 点赞文章时增加积分

```java
public void likeArticle(Long articleId, Long userId) {
    // 检查是否已经点赞
    QueryWrapper<Like> wrapper = new QueryWrapper<>();
    wrapper.eq("like_type", "article");
    wrapper.eq("id", articleId);
    wrapper.eq("user_id", userId);
    Like existingLike = likeMapper.selectOne(wrapper);

    if (existingLike == null) {
        // 保存点赞记录
        Like like = new Like();
        like.setUserId(userId);
        like.setId(articleId);
        like.setLikeType("article");
        like.setCreateTime(LocalDateTime.now());
        likeMapper.insert(like);

        // 发送积分消息
        mqUtil.sendMessage(
            RabbitMQConfig.USER_ACTION_EXCHANGE,
            "score.like",
            ScoreMessage.builder()
                .userId(userId)
                .score(ScoreConstants.ScoreRule.LIKE_ARTICLE)
                .action("like")
                .resourceId(articleId)
                .build()
        );
    }
}
```

### 1.4 评论文章时增加积分

```java
public void addComment(Comment comment) {
    comment.setCreateTime(LocalDateTime.now());
    save(comment);

    // 发送积分消息
    mqUtil.sendMessage(
        RabbitMQConfig.USER_ACTION_EXCHANGE,
        "score.comment",
        ScoreMessage.builder()
            .userId(comment.getUserId())
            .score(ScoreConstants.ScoreRule.COMMENT_ARTICLE)
            .action("comment")
            .resourceId(comment.getArticleId())
            .build()
    );
}
```

## 2. 通知消息发送

### 2.1 文章被点赞时发送通知

```java
public void likeArticle(Long articleId, Long userId) {
    // ... 保存点赞记录代码省略 ...

    // 获取文章信息
    Article article = articleMapper.getById(articleId);
    User liker = userMapper.selectById(userId);

    // 发送通知给文章作者
    mqUtil.sendMessage(
        RabbitMQConfig.NOTIFICATION_EXCHANGE,
        "notification.like",
        NotificationMessage.builder()
            .type(NotificationConstants.NotificationType.LIKE)
            .senderId(userId)
            .receiverId(article.getAuthorId())
            .title("收到新点赞")
            .content(liker.getUsername() + " 点赞了你的文章《" + article.getTitle() + "》")
            .resourceId(articleId)
            .resourceType(NotificationConstants.ResourceType.ARTICLE)
            .build()
    );
}
```

### 2.2 文章被评论时发送通知

```java
public void addComment(Comment comment) {
    // ... 保存评论记录代码省略 ...

    // 获取文章信息
    Article article = articleMapper.getById(comment.getArticleId());
    User commenter = userMapper.selectById(comment.getUserId());

    // 发送通知给文章作者
    mqUtil.sendMessage(
        RabbitMQConfig.NOTIFICATION_EXCHANGE,
        "notification.comment",
        NotificationMessage.builder()
            .type(NotificationConstants.NotificationType.COMMENT)
            .senderId(comment.getUserId())
            .receiverId(article.getAuthorId())
            .title("收到新评论")
            .content(commenter.getUsername() + " 评论了你的文章《" + article.getTitle() + "》")
            .resourceId(comment.getArticleId())
            .resourceType(NotificationConstants.ResourceType.ARTICLE)
            .build()
    );
}
```

### 2.3 评论被回复时发送通知

```java
public void replyComment(Comment reply) {
    // ... 保存回复记录代码省略 ...

    // 获取原评论
    Comment parentComment = getById(reply.getParentId());
    User replier = userMapper.selectById(reply.getUserId());

    // 发送通知给原评论作者
    mqUtil.sendMessage(
        RabbitMQConfig.NOTIFICATION_EXCHANGE,
        "notification.comment",
        NotificationMessage.builder()
            .type(NotificationConstants.NotificationType.COMMENT)
            .senderId(reply.getUserId())
            .receiverId(parentComment.getUserId())
            .title("收到新回复")
            .content(replier.getUsername() + " 回复了你的评论")
            .resourceId(reply.getId())
            .resourceType(NotificationConstants.ResourceType.COMMENT)
            .build()
    );
}
```

### 2.4 发送系统通知

```java
public void sendSystemNotification(List<Long> userIds, String title, String content) {
    for (Long userId : userIds) {
        mqUtil.sendMessage(
            RabbitMQConfig.NOTIFICATION_EXCHANGE,
            "notification.system",
            NotificationMessage.builder()
                .type(NotificationConstants.NotificationType.SYSTEM)
                .senderId(0L) // 系统发送的通知，发送者ID为0
                .receiverId(userId)
                .title(title)
                .content(content)
                .build()
        );
    }
}
```

## 3. 配置说明

RabbitMQ 配置使用了两个交换机和两个队列：

1. **用户行为交换机**(`user.action.exchange`): 处理用户行为相关的消息，如点赞、评论、发布文章等，主要用于积分计算
2. **通知交换机**(`notification.exchange`): 处理通知相关的消息，用于向用户发送通知

积分和通知消息分开处理，可以使系统更加灵活和可扩展。
