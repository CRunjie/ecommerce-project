<template>
<el-dialog v-model="dialogVisible"   @close="gogo(1)" width="40%">
    <div class="box1">
      <div class="box2">
        <img :src="getAssetsFile(goods.gpicture)" class="image"/>
     </div>
     <div class="box3">
          <p class="myfont"> 商品名：<span>{{goods.gname}}</span></p>
          <p class="myfont"> 原价：<span>&yen;<strike>{{goods.goprice}}</strike></span></p>
          <p> <span class="myfont">折扣价：</span><span style="color: rgb(249, 7, 7); font-size: 10pt;">&yen;{{goods.grprice}}</span></p>
          <p class="myfont"> 库存：<span>{{goods.gstore}}</span></p>
          <p> <el-input v-model="inputvalue" @input="handleEdit" class="w-50 m-2" size="small" placeholder="请输入购买量" /></p>
          <p>
            <el-button type="primary" :icon="ShoppingCart" class="button"  @click="gogo(2)" size="small">加入购物车</el-button>
            <el-button type="warning" :icon="Shop" class="button" size="small" @click="gogo(3)">立刻购买</el-button>
            <el-button type="success" :icon="CirclePlusFilled" class="button" size="small" @click="gogo(4)">收藏</el-button>
          </p>
    </div>
   </div>
</el-dialog>
</template>
<script setup>
import { useRouter} from 'vue-router'
import { onMounted, ref, getCurrentInstance } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ShoppingCart, CirclePlusFilled, Shop } from '@element-plus/icons-vue'
import axios from '../axios/request.js'
const router = useRouter()//相当于this.$router，一般具有功能性，例如路由跳转
const dialogVisible = true
const inputvalue = ref('1')
//接收传递过来的数据
const historyParams = history.state
let goods = ref({})
onMounted (()=> {
    goods.value =  historyParams;
})
// 获取assets静态资源
const getAssetsFile = (url) => {
  return new URL(`../assets/${url}`, import.meta.url).href;
}
//只能输入正整数
const handleEdit = (e)  => {
      let value = e.replace(/^(0+)|[^\d]+/g,''); // 以0开头或者输入非数字，会被替换成空
      value = value.replace(/(\d{15})\d*/, '$1') // 最多保留15位整数
      inputvalue.value = value
}
const gogo = (myValue) => {
    //关闭
    if(myValue === 1){
      router.go(-1)
      return
    } else {
      if(sessionStorage.getItem('bemail') === null) {
        alert("您没有登录，请登录！")
        router.replace('/login')
        return false
      }
      if(myValue === 2 || myValue === 3 ){//加入购物车或立即购买
        if(inputvalue.value === ''){
            ElMessageBox.alert(
              '<span style="color: rgb(249, 7, 7); font-size: 12pt;">请输入购买量！</span>',
              '',
              {
                dangerouslyUseHTMLString: true,
              }
            )
            return false
        }
        if(inputvalue.value > goods.value.gstore){
          ElMessageBox.alert(
              '<span style="color: rgb(249, 7, 7); font-size: 12pt;">库存不足！</span>',
              '',
              {
                dangerouslyUseHTMLString: true,
              }
            )
            return false
        }
        if(myValue === 2){//加入购物车
          axios.myPost('/api/before/cart/add',
          {
            busertableId: sessionStorage.getItem('bid'),
            goodstableId: goods.value.id,
            shoppingnum: inputvalue.value
          })
          .then(res => {
            if(res.data.code  === 200){
              ElMessage.success({message: '成功加入购物车',type: 'success'})
              router.push({name: 'mycart'})
            } else {
              ElMessage.error('加入失败！')
            } 
          })
          .catch((error) => {
              ElMessage.error(error)
          })
        } else {//立刻购买，就是直接提交订单
          let gids = [goods.value.id]
          let shoppingnums = [inputvalue.value]
          axios.myPost('/api/before/orders/submitOrder',
          {
            bgid: gids,
            bshoppingnum: shoppingnums,
            busertableId: sessionStorage.getItem('bid'),
            amount: goods.value.grprice * inputvalue.value
          }).then(res => {
              if(res.data.resData.id > 0){
                ElMessage.success({message: '订单提交成功，请付款！',type: 'success'})
                router.push({name: 'index'})
              } else
                ElMessage.error('订单提交失败！')
          })
          .catch((error) => {
              ElMessage.error(error)
          })
        }
      } else {//收藏
        //headers放在第三个参数
        axios.myPost('/api/before/focus/add',
        {
          busertableId: sessionStorage.getItem('bid'),
          goodstableId: goods.value.id
        })
        .then(res => {
          if(res.data.code  === 307){
            ElMessage.error(res.data.msg)
          } else if (res.data.code  === 200) {
            ElMessage.success({message: '成功关注该商品',type: 'success'})
          } else{
            ElMessage.error(res.data.coede + ' ：添加失败')
          }
        })
        .catch((error) => {
            ElMessage.error(error)
        })
      }
    }
  }
</script>

<style scoped>
.myfont {
  font-size: 10pt;
  color: rgb(125, 123, 123);
}
.image {
  width: 230px;
  height: 200px; 
  display: block;
}
.box1 {
  width:460px;
  display: flex;
  justify-content: space-between;
}
.box2 {
  width:230px;
}
.box3 {
  width:210px;
}
.button {
  padding: 0;
  min-height: auto;
}
</style>