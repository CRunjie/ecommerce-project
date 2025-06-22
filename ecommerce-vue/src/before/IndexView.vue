/* eslint-disable */
<template>
  <div>
    <HeaderView @goIndex="goToIndex" @searchIndex="searchToIndex"/>
  </div>
  <div>
    <el-row>
        <el-col
        v-for="(item, index) in goodslists"
        :key="item.id"
        :span="4"
        :offset="index > 0 && (index ==1 || (index !=1 && index % 5 != 0))? 1 : 0">
        <!--offset左侧的间隔栅数，一共24栅-->
            <el-card :body-style="{ padding: '5px' }">
                <el-link :underline="false" @click="goToGoodsDetail(item)">
                  <img :src="getAssetsFile(item.gpicture)" class="image"/>
                </el-link>
                <div style="padding: 5px">
                    <el-link :underline="false" @click="goToGoodsDetail(item)"><span class="myfont">{{item.gname}}</span></el-link>
                    <br>
                    <span class="myfont">&yen;<strike>{{item.goprice.toFixed(1)}}</strike></span> &nbsp; 
                    <span class="yourfont">&yen;{{item.grprice.toFixed(1)}}</span>
                </div>
            </el-card>
        </el-col>
    </el-row>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import HeaderView from '../components/HeaderView.vue'
import {useRouter} from 'vue-router'
import { ElMessage} from 'element-plus'
const router = useRouter()
import axios from '../axios/request.js'
let goodslists = ref([])
onMounted (()=> {
    goToIndex(0)
})
// 获取assets静态资源
const getAssetsFile = (url) => {
  return new URL(`../assets/${url}`, import.meta.url).href;
}
//typeid子组件传递过来的数据
const goToIndex = (typeid) => {
  axios.myPost('/api/before/index/getGoodsIndex',
  {
    goodstypeId: typeid
  }
  ).then(res => {
      goodslists.value =  res.data.resData;
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
//searchV子组件传递过来的数据
const searchToIndex = (searchV) => {
  axios.myPost('/api/before/index/getGoodsIndex',
  {
    gname: searchV
  }
  ).then(res => {
      goodslists.value =  res.data.resData;
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
const goToGoodsDetail = (goods) => {
    //从Vue Router的2022-8-22 更新后，弃用params传参
    //使用 History API 方式传递和接收， 在跳转前的页面使用 state 参数
    router.push({name: 'goodsDetail', state: goods})
}
</script>
<style scoped>
.myfont {
  font-size: 10pt;
  color: rgb(125, 123, 123);
}
.yourfont {
  font-size: 11pt;
  color: rgb(249, 7, 7);
}
.image {
  width: 240px;
  height: 180px; 
  display: block;
}
.el-col {
  margin-bottom: 10px;
}
</style>