# Android 工具库
将 [RedrockExamUtils](https://github.com/haruue/RedrockExamUtils) 的 util 模块单独取出以便维护    

## Overview
[RedrockExamUtils](https://github.com/haruue/RedrockExamUtils) 是为了应付红岩期末考核封装的，很多东西只是简单的弄了下，并不是很好，~~所以就丢掉吧。（反正以后不会有这些限制了就直接用网上现成的轮子吧！）~~    
~~其实总的看了下也就还有这个模块有些卵用~~    
因此我把这个模块单独拿出来，虽然功能很不完善，但是我仍然希望继续完善它。。。

## Download
``` Gradle
repositories {
    maven {
        url 'https://dl.bintray.com/haruue/maven/'
    }
}

dependencies {
    compile 'moe.haruue:haruueutils:1.0.0'
}

```

## Function
| 类 | 功能 |
|--------------------|--------------------|
| ActivityManager | 一个 Activity 管理器 |
| EncryptUtils | 字符串加密、解密、编码、解码 |
| InstanceSaver | savedInstance 自动存储和恢复 |
| RegExUtils | 常用的正则表达式匹配工具 |
| SharedPreferencesUtils | 方便的 SharedPreferences 操作 |
| StandardUtils | 标准工具：log, toast 等等 |
| ThreadUtils | 线程工具：绑定线程到 Activity 等等 |

具体功能参考源代码中的 Javadoc

## License
``` License
Copyright 2016 Haruue Icymoon

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```