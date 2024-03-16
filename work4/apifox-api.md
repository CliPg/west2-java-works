# work4

exported at 2024-03-16 10:49:17

## VideoController

VideoController


---
### publish

> BASIC

**Path:** /video/publish

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| Content-Type | application/json | YES |  |
| token |  | YES |  |

**Request Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| id | string |  |
| userId | string |  |
| videoUrl | string |  |
| coverUrl | string |  |
| title | string |  |
| description | string |  |
| visitCount | integer |  |
| likeCount | integer |  |
| commentCount | integer |  |
| createTime | string |  |
| updateTime | string |  |
| deleteTime | string |  |

**Request Demo:**

```json
{
  "id": "",
  "userId": "",
  "videoUrl": "",
  "coverUrl": "",
  "title": "",
  "description": "",
  "visitCount": 0,
  "likeCount": 0,
  "commentCount": 0,
  "createTime": "",
  "updateTime": "",
  "deleteTime": ""
}
```



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### list

> BASIC

**Path:** /video/list

**Method:** GET

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| userId |  | YES |  |
| pageNum |  | YES |  |
| pageSize |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### search

> BASIC

**Path:** /video/search

**Method:** POST

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| keywords |  | YES |  |
| pageNum |  | YES |  |
| pageSize |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### popular

> BASIC

**Path:** /video/popular

**Method:** GET

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| pageNum |  | YES |  |
| pageSize |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```





## InteractionController

InteractionController


---
### likeAction

> BASIC

**Path:** /like/action

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| videoId |  | YES |  |
| actionType |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### likeList

> BASIC

**Path:** /like/list

**Method:** GET

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| userId |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### commentPublish

> BASIC

**Path:** /comment/publish

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| videoId |  | YES |  |
| comment |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### commentList

> BASIC

**Path:** /comment/list

**Method:** GET

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| videoId |  | YES |  |
| pageNum |  | YES |  |
| pageSize |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### commentDelet

> BASIC

**Path:** /comment/delete

**Method:** DELETE

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| videoId |  | YES |  |
| comment |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```





## UserController

UserController


---
### login

> BASIC

**Path:** /user/login

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| Content-Type | application/json | YES |  |

**Request Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| id | string |  |
| username | string |  |
| password | string | @TableField(select = false) |
| avatarUrl | string |  |
| createTime | string |  |
| updateTime | string |  |
| deleteTime | string |  |

**Request Demo:**

```json
{
  "id": "",
  "username": "",
  "password": "",
  "avatarUrl": "",
  "createTime": "",
  "updateTime": "",
  "deleteTime": ""
}
```



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### register

> BASIC

**Path:** /user/register

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| Content-Type | application/json | YES |  |

**Request Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| id | string |  |
| username | string |  |
| password | string | @TableField(select = false) |
| avatarUrl | string |  |
| createTime | string |  |
| updateTime | string |  |
| deleteTime | string |  |

**Request Demo:**

```json
{
  "id": "",
  "username": "",
  "password": "",
  "avatarUrl": "",
  "createTime": "",
  "updateTime": "",
  "deleteTime": ""
}
```



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### info

> BASIC

**Path:** /user/info

**Method:** GET

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| id |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### avatarUpload

> BASIC

**Path:** /user/avatar/upload

**Method:** PUT

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| avatarUrl |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```





## SocialController

SocialController


---
### follow

> BASIC

**Path:** /relation/action

**Method:** POST

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| followingId |  | YES |  |
| actionType |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### followingList

> BASIC

**Path:** /following/list

**Method:** GET

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| userId |  | YES |  |
| pageNum |  | YES |  |
| pageSize |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### followerList

> BASIC

**Path:** /follower/list

**Method:** GET

> REQUEST

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| userId |  | YES |  |
| pageNum |  | YES |  |
| pageSize |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```




---
### friendsList

> BASIC

**Path:** /friends/list

**Method:** GET

> REQUEST

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| token |  | YES |  |

**Query:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| pageNum |  | YES |  |
| pageSize |  | YES |  |



> RESPONSE

**Headers:**

| name | value | required | desc |
| ------------ | ------------ | ------------ | ------------ |
| content-type | application/json;charset=UTF-8 | NO |  |

**Body:**

| name | type | desc |
| ------------ | ------------ | ------------ |
| code | integer | 状态码 |
| msg | string | 提示信息，如果有错误时，前端可以获取该字段进行提示 |
| data | object | 查询到的结果数据， |

**Response Demo:**

```json
{
  "code": 0,
  "msg": "",
  "data": {}
}
```





