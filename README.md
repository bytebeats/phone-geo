# phone-geo
中国大陆手机号归属地查询.<br>
Kotlin版本根据手机号确定手机号运营商即归属地, 支持包括虚拟运营商的中国大陆手机号查询.

##Effect/效果图

<img src="/media/phone-geo.gif" width="360" height="720"/>

## 数据源

数据源`dat`文件来自[xluohome/phonedata](https://github.com/xluohome/phonedata)提供的数据库, 会不定时同步更新数据库

归属地信息库最后更新：**2021年06月**

## How to use/如何使用
```
        lookup.setOnClickListener {
            PhoneNumberLookup.instance().with(LookupAlgorithm.IMPL.SEQUENCE).lookup(editor.text.toString())
                ?.apply {
                    province.text = geoInfo.province
                    city.text = geoInfo.city
                    zipCode.text = geoInfo.zipCode
                    areaCode.text = geoInfo.areaCode
                    ispText.text = isp.carrier
                } ?: kotlin.run {
                province.text = UNKNOWN
                city.text = UNKNOWN
                zipCode.text = UNKNOWN
                areaCode.text = UNKNOWN
                ispText.text = UNKNOWN
            }
        }
```

## 感谢
- 感谢[xluohome/phonedata](https://github.com/xluohome/phonedata)共享的数据库
- 也参考了@EeeMt 的java实现[EeeMt/phone-number-geo](https://github.com/EeeMt/phone-number-geo)