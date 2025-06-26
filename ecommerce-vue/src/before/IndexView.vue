/* eslint-disable */
<template>
  <div>
    <HeaderView @goIndex="goToIndex" @searchIndex="searchToIndex"/>
  </div>
  <div class="doll-container">
    <el-row>
        <el-col
        v-for="(item, index) in goodslists"
        :key="item.id"
        :span="4"
        :offset="index > 0 && (index ==1 || (index !=1 && index % 5 != 0))? 1 : 0">
        <!--offset左侧的间隔栅数，一共24栅-->
            <el-card :body-style="{ padding: '5px' }" class="doll-card">
                <el-link :underline="false" @click="goToGoodsDetail(item)">
                  <img :src="getAssetsFile(item.gpicture)" class="image doll-image"/>
                </el-link>
                <div style="padding: 10px">
                    <el-link :underline="false" @click="goToGoodsDetail(item)" class="doll-name-link">
                      <span class="doll-name">{{item.gname}}</span>
                    </el-link>
                    <br>
                    <span class="doll-original-price">&yen;<strike>{{item.goprice.toFixed(1)}}</strike></span> &nbsp; 
                    <span class="doll-current-price">&yen;{{item.grprice.toFixed(1)}}</span>
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
.doll-container {
  padding: 20px 0;
}
.doll-card {
  transition: all 0.3s ease;
  border-radius: 12px;
  overflow: hidden;
  border: none;
  box-shadow: 0 6px 16px rgba(0,0,0,0.08);
}
.doll-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(var(--doll-primary), 0.15);
}
.doll-name {
  font-size: 14px;
  color: var(--doll-dark);
  font-weight: 500;
  display: block;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.doll-name-link:hover .doll-name {
  color: var(--doll-primary);
}
.doll-original-price {
  font-size: 12px;
  color: #999;
}
.doll-current-price {
  font-size: 16px;
  color: var(--doll-primary);
  font-weight: bold;
}
.doll-image {
  width: 100%;
  height: 180px; 
  display: block;
  object-fit: cover;
  transition: transform 0.5s;
}
.doll-image:hover {
  transform: scale(1.05);
}
.el-col {
  margin-bottom: 20px;
}
</style>