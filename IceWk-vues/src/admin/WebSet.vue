<template>
  <div class="">
    <el-tabs tab-position="left" style="height: 800px">
      <el-tab-pane label="网站配置" style="width: 420px">
        
        <div class="sitpage">
          <h4>网站标题</h4>
          <el-input v-model="postList.sitTitle" placeholder="请输入内容"></el-input>
          <h4>网站LOGO</h4>
          <!-- <el-upload
            class="avatar-uploader"
            action="https://jsonplaceholder.typicode.com/posts/"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload> -->

          <h4>版权</h4>
          <el-input v-model="postList.banquan" placeholder="请输入内容"></el-input>
          <h4>备案号</h4>
          <el-input v-model="postList.beian" placeholder="请输入内容"></el-input>
          <h4>禁用评论</h4>
          <el-switch
            v-model="postList.comment_show"
            active-color="#13ce66"
            inactive-color="#ff4949"
          >
          </el-switch>
        </div>
        <el-row class="sitpagesitmap">
          <el-button type="danger" round>取消</el-button>
          <el-button type="primary" @click="sitmap()" round>保存</el-button>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="资源配置">配置管理</el-tab-pane>
      <el-tab-pane label="角色管理">角色管理</el-tab-pane>
      <el-tab-pane label="定时任务补偿">定时任务补偿</el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import {mapState,mapMutations} from 'vuex'

import { setSetting } from '@/api/setting'
import { getSetting } from '@/api/websetting'

export default {
  methods: {
    sitmap(){
     
      setSetting(this.postList).then(resp=>{
      console.log(resp)
      if(resp.data === 1){
        console.log("success")
         this.$notify({
                title: '成功',
                message: '修改成功',
                type: 'success',
                duration: 2000
              })
      }else{
        console.log("no")
      }
    })
    },
    fulldata(){
     getSetting().then(resp => {   
      this.postList.sitTitle = resp.data.sitTitle
      this.postList.sitLogo = resp.data.sitLogo
      this.postList.banquan = resp.data.banquan
      this.postList.beian = resp.data.beian
      this.postList.comment_show = resp.data.comment_show
    })
    
    },
    handleClick(tab, event) {
      console.log(tab, event);
    }
  },
    computed:{
       ...mapState(['playlist','glabledata','count'])
        },
  async created() {
    await this.fulldata();
  },
  data() {
    return {
      postList: {
        id: 1,
        sitTitle: "",
        sitLogo: "",
        banquan: "",
        beian: "",
        comment_show: true,
      }

    };
  }
};
</script>
<style scoped>
.sitpagesitmap {
  margin-top: 20px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>