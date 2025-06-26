<template>
    <el-container>
      <el-header style="height: 30px; background-color: var(--doll-light)">
        <el-row align="middle" style="margin-top: 5px">
          <el-col :span="3"><div class="coldiv doll-brand">欢迎光临玩偶世界</div></el-col>
          <el-col :span="1">
            <div class="coldiv">
              <el-link @click="register" :underline="false" class="doll-link">注册</el-link>
            </div>
          </el-col>
          <el-col :span="1">
            <div class="coldiv">
              <span v-if="userName != null" class="doll-user">{{userName}}</span>
              <el-link @click="login" v-if="userName === null" :underline="false" class="doll-link">登录</el-link>
            </div>
          </el-col>
          <el-col :span="7"><div></div></el-col>
          <el-col :span="2">
            <div class="coldiv">
              <el-link @click="myselfinfo"  :icon="InfoFilled" :underline="false" class="doll-link">个人信息</el-link>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="coldiv">
              <el-link @click="mycart" :icon="ShoppingCartFull"  :underline="false" class="doll-link">我的购物车</el-link>
            </div></el-col>
          <el-col :span="2">
            <div class="coldiv">
              <el-link @click="myfocus" :icon="WalletFilled"  :underline="false" class="doll-link">我的收藏</el-link>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="coldiv">
              <el-link @click="myorder" :icon="ShoppingBag" :underline="false" class="doll-link">我的订单</el-link>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="coldiv">
              <el-link  @click="loginOut" :icon="WarningFilled" :underline="false" class="doll-link">安全退出</el-link>
            </div>
          </el-col>
          <el-col :span="2">
            <div class="coldiv">
              <el-link  @click="adminLogin" :icon="UserFilled" :underline="false" class="doll-link">管理员</el-link>
            </div>
          </el-col>
        </el-row>
      </el-header>
      <div>
        <el-carousel  :interval="5000" arrow="always" height="200px" class="doll-carousel">
          <el-carousel-item v-for="item in imgList" :key="item.id">
            <el-link :underline="false" @click="goToGoodsDetail(item)">
              <img :src="getAssetsFile(item.gpicture)" :title="item.gname" alt="图片丢失了" style="height:100%; width:100%;"/>
            </el-link>
          </el-carousel-item>
        </el-carousel>
      </div>
      <el-footer style="height: 35px; background-color: var(--doll-light)" class="doll-footer">
        <el-row  style="margin-top: 5px">
          <el-col :span="18">
            <el-row>
              <!--key为了高效的更新虚拟DOM-->
              <el-col :span="2">
                  <div class="coldiv">
                    <el-link @click="toIndex(0)" :icon="HomeFilled" :underline="false" class="doll-link">首页</el-link>
                  </div>
              </el-col>
              <el-col :span="2" v-for="item in goodstypes" :key="item.id">
                  <div class="coldiv">
                    <el-link @click="toIndex(item.id)"  :underline="false" class="doll-link">{{item.typename}}</el-link>
                  </div>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="6">
              <el-form :inline="true" :model="searchForm" class="demo-form-inline" size="small">
                <el-form-item>
                  <el-input v-model="searchForm.gname" placeholder="搜索心仪的玩偶" class="doll-search" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="toSearchIndex()" :icon="Search" class="doll-btn">搜索</el-button>
                </el-form-item>
              </el-form>
          </el-col>
        </el-row>
      </el-footer>
    </el-container>
</template>
<script setup>
import { onMounted, reactive, ref} from 'vue'
import { useRouter} from 'vue-router'
import { Search,
   ShoppingCartFull, 
   InfoFilled,
   UserFilled, 
   WalletFilled, 
   HomeFilled, 
   ShoppingBag,
  WarningFilled} from '@element-plus/icons-vue'
import { ElMessage} from 'element-plus'
import axios from '../axios/request.js'
const router = useRouter()
const userName = sessionStorage.getItem("bemail")
//使用defineEmits声明向父组件抛出的自定义事件
const myemits = defineEmits(['goIndex', 'searchIndex'])
let imgList = ref([])
let goodstypes = ref([])
const searchForm = reactive({
  gname: ''
})
onMounted(()=>{
  getAdvGoods()
  getGoodsType()
})
// 获取assets静态资源
const getAssetsFile = (url) => {
  return new URL(`../assets/${url}`, import.meta.url).href;
}
const goToGoodsDetail = (goods) => {
    router.push({name: 'goodsDetail', state: goods})
}
const toIndex = (typeid) => {
  //通过抛出goIndex事件向父组件传值
  myemits('goIndex', typeid)
}
const toSearchIndex = () => {
  //通过抛出goIndex事件向父组件传值
  myemits('searchIndex', searchForm.gname)
}
const loginOut = () => {
  sessionStorage.removeItem("bemail")
  sessionStorage.removeItem("token")
  sessionStorage.removeItem("bid")
  ElMessage({message: '成功安全退出！', type: 'success'})
  //刷新当前页
  router.go(0)
}
const myorder = () => {
  router.push({name: 'myorder'})
}
const register = () => {
  router.push({name: 'register'})
}
const login = () => {
  router.push({name: 'login'})
}
const mycart = () => {
  router.push({name: 'mycart'})
}
const myselfinfo = () => {
  router.push({name: 'myselfinfo'})
}
const myfocus = () => {
  router.push({name: 'myfocus'})
}
const adminLogin =() =>{
  router.push({name: 'admin-login'})
}
//获得广告商品
const getAdvGoods = ()=> {
  axios.myGet('/api/before/index/getAdvGoods')
  .then(res => {
      imgList.value =  res.data.resData;
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
//获得商品类型
const getGoodsType = ()=> {
  axios.myGet('/api/before/index/getAllGoodsType')
  .then(res => {
      goodstypes.value =  res.data.resData;
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
</script>
<style scoped>
.coldiv {
  font-size: 11pt;
  color: var(--doll-text);
}
.doll-brand {
  font-weight: bold;
  color: var(--doll-primary);
  font-size: 13pt;
}
.doll-link {
  color: var(--doll-secondary);
  transition: all 0.3s;
}
.doll-link:hover {
  color: var(--doll-primary);
  transform: scale(1.05);
}
.doll-user {
  color: var(--doll-primary);
  font-weight: bold;
}
.doll-carousel {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 10px;
}
.doll-footer {
  border-top: 2px dashed var(--doll-accent);
  margin-top: 5px;
}
.doll-search .el-input__inner {
  border-radius: 20px;
  border: 2px solid var(--doll-accent);
}
.doll-btn {
  background: var(--doll-primary);
  border-color: var(--doll-primary);
  border-radius: 20px;
}
.doll-btn:hover {
  background: var(--doll-secondary);
  border-color: var(--doll-secondary);
}
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
  text-align: center;
}
.el-carousel__item:nth-child(2n) {
  background-color: var(--doll-light);
}
.el-carousel__item:nth-child(2n + 1) {
  background-color: var(--doll-accent);
}
</style>