<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户在小程序端的 openId 唯一" prop="openid">
        <el-input
          v-model="queryParams.openid"
          placeholder="请输入用户在小程序端的 openId 唯一"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="派币余额" prop="money">
        <el-input
          v-model="queryParams.money"
          placeholder="请输入派币余额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户等级" prop="userLevel">
        <el-input
          v-model="queryParams.userLevel"
          placeholder="请输入用户等级"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户累计积分" prop="integration">
        <el-input
          v-model="queryParams.integration"
          placeholder="请输入用户累计积分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户现有积分" prop="integrationNow">
        <el-input
          v-model="queryParams.integrationNow"
          placeholder="请输入用户现有积分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="真实姓名" prop="realname">
        <el-input
          v-model="queryParams.realname"
          placeholder="请输入真实姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input
          v-model="queryParams.nickname"
          placeholder="请输入昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户性别 0 男  1 女  2 未知" prop="sex">
        <el-select v-model="queryParams.sex" placeholder="请选择用户性别 0 男  1 女  2 未知" clearable>
          <el-option
            v-for="dict in dict.type.sys_user_sex"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="个人介绍" prop="intro">
        <el-input
          v-model="queryParams.intro"
          placeholder="请输入个人介绍"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input
          v-model="queryParams.age"
          placeholder="请输入年龄"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="星座" prop="constellation">
        <el-select v-model="queryParams.constellation" placeholder="请选择星座" clearable>
          <el-option
            v-for="dict in dict.type.constellation"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="人格类型" prop="mbti">
        <el-select v-model="queryParams.mbti" placeholder="请选择人格类型" clearable>
          <el-option
            v-for="dict in dict.type.mbti"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="兴趣爱好" prop="hobby">
        <el-input
          v-model="queryParams.hobby"
          placeholder="请输入兴趣爱好"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学校" prop="school">
        <el-input
          v-model="queryParams.school"
          placeholder="请输入学校"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="职业" prop="occupation">
        <el-input
          v-model="queryParams.occupation"
          placeholder="请输入职业"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态 是否被封禁" prop="state">
        <el-select v-model="queryParams.state" placeholder="请选择状态 是否被封禁" clearable>
          <el-option
            v-for="dict in dict.type.state"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:pzc_user:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:pzc_user:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:pzc_user:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:pzc_user:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pzc_userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户主键" align="center" prop="userId" v-if="true"/>
      <el-table-column label="用户在小程序端的 openId 唯一" align="center" prop="openid" />
      <el-table-column label="派币余额" align="center" prop="money" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="success" @click="handlerUpdateMoney(scope.row)">修改</el-button>
        </template>
      </el-table-column>
      <el-table-column label="用户等级" align="center" prop="userLevel">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.user_level" :value="scope.row.userLevel"/>
        </template>
      </el-table-column>
      <el-table-column label="用户累计积分" align="center" prop="integration" />
      <el-table-column label="用户现有积分" align="center" prop="integrationNow" />
      <el-table-column label="真实姓名" align="center" prop="realname" />
      <el-table-column label="昵称" align="center" prop="nickname" />
      <el-table-column label="用户性别 0 女  1 男  2 未知" align="center" prop="sex">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.sex"/>
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center" prop="phone" />
      <el-table-column label="头像" align="center" prop="avatar" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.avatar" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="地址" align="center" prop="address" />
      <el-table-column label="个人介绍" align="center" prop="intro" />
      <el-table-column label="年龄" align="center" prop="age" />
      <el-table-column label="星座" align="center" prop="constellation">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.constellation" :value="scope.row.constellation"/>
        </template>
      </el-table-column>
      <el-table-column label="人格类型" align="center" prop="mbti">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.mbti" :value="scope.row.mbti"/>
        </template>
      </el-table-column>
      <el-table-column label="兴趣爱好" align="center" prop="hobby" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.hobby" :value="scope.row.user_hobby"/>
        </template>
      </el-table-column>
      <el-table-column label="学校" align="center" prop="school" />
      <el-table-column label="职业" align="center" prop="occupation" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="喜欢的音乐风格" align="center" prop="musicStyle">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.music_style" :value="scope.row.musicStyle ? scope.row.musicStyle.split(',') : []"/>
        </template>
      </el-table-column>
      <el-table-column label="状态 是否被封禁" align="center" prop="state">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.state" :value="scope.row.state"/>
        </template>
      </el-table-column>

    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户在小程序端的 openId 唯一" prop="openid">
          <el-input v-model="form.openid" placeholder="请输入用户在小程序端的 openId 唯一" />
        </el-form-item>
        <el-form-item label="派币余额" prop="money">
          <el-input v-model="form.money" placeholder="请输入派币余额" />
        </el-form-item>
        <el-form-item label="用户等级" prop="userLevel">
          <el-input v-model="form.userLevel" placeholder="请输入用户等级" />
        </el-form-item>
        <el-form-item label="用户累计积分" prop="integration">
          <el-input v-model="form.integration" placeholder="请输入用户累计积分" />
        </el-form-item>
        <el-form-item label="用户现有积分" prop="integrationNow">
          <el-input v-model="form.integrationNow" placeholder="请输入用户现有积分" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realname">
          <el-input v-model="form.realname" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="用户性别 0 男  1 女  2 未知" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio
              v-for="dict in dict.type.sys_user_sex"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <image-upload v-model="form.avatar"/>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="个人介绍" prop="intro">
          <el-input v-model="form.intro" placeholder="请输入个人介绍" />
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="form.age" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="星座" prop="constellation">
          <el-select v-model="form.constellation" placeholder="请选择星座">
            <el-option
              v-for="dict in dict.type.constellation"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="人格类型" prop="mbti">
          <el-select v-model="form.mbti" placeholder="请选择人格类型">
            <el-option
              v-for="dict in dict.type.mbti"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="兴趣爱好" prop="hobby">
          <el-input v-model="form.hobby" placeholder="请输入兴趣爱好" />
        </el-form-item>
        <el-form-item label="学校" prop="school">
          <el-input v-model="form.school" placeholder="请输入学校" />
        </el-form-item>
        <el-form-item label="职业" prop="occupation">
          <el-input v-model="form.occupation" placeholder="请输入职业" />
        </el-form-item>
        <el-form-item label="喜欢的音乐风格" prop="musicStyle">
          <el-checkbox-group v-model="form.musicStyle">
            <el-checkbox
              v-for="dict in dict.type.music_style"
              :key="dict.value"
              :label="dict.value">
              {{dict.label}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="状态 是否被封禁" prop="state">
          <el-select v-model="form.state" placeholder="请选择状态 是否被封禁">
            <el-option
              v-for="dict in dict.type.state"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {addPzc_user, delPzc_user, getPzc_user, listPzc_user, update_money, updatePzc_user} from "@/api/system/pzc_user";

export default {
  name: "Pzc_user",
  dicts: ['sys_user_sex', 'music_style', 'mbti', 'state', 'constellation','user_hobby'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      pzc_userList: [],

      newMoney: {
        userId: undefined,
        money: undefined
      },
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        openid: undefined,
        money: undefined,
        userLevel: undefined,
        integration: undefined,
        integrationNow: undefined,
        realname: undefined,
        nickname: undefined,
        sex: undefined,
        phone: undefined,
        avatar: undefined,
        address: undefined,
        intro: undefined,
        age: undefined,
        constellation: undefined,
        mbti: undefined,
        hobby: undefined,
        school: undefined,
        occupation: undefined,
        musicStyle: undefined,
        state: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户主键不能为空", trigger: "blur" }
        ],
        openid: [
          { required: true, message: "用户在小程序端的 openId 唯一不能为空", trigger: "blur" }
        ],
        nickname: [
          { required: true, message: "昵称不能为空", trigger: "blur" }
        ],
        sex: [
          { required: true, message: "用户性别 0 男  1 女  2 未知不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listPzc_user(this.queryParams).then(response => {
        this.pzc_userList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        openid: undefined,
        money: undefined,
        userLevel: undefined,
        integration: undefined,
        integrationNow: undefined,
        realname: undefined,
        nickname: undefined,
        sex: undefined,
        phone: undefined,
        avatar: undefined,
        address: undefined,
        intro: undefined,
        age: undefined,
        constellation: undefined,
        mbti: undefined,
        hobby: undefined,
        school: undefined,
        occupation: undefined,
        createTime: undefined,
        musicStyle: [],
        updateTime: undefined,
        state: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({ path: "/system/pzc_user/todoList" })
      // this.reset();
      // this.open = true;
      // this.title = "添加用户";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const userId = row.userId || this.ids
      getPzc_user(userId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.form.musicStyle = this.form.musicStyle.split(",");
        this.open = true;
        this.title = "修改用户";
      });
    },

    handlerUpdateMoney(row)
    {
      this.loading = true;
      this.$prompt('请输入用户余额', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({ value }) => {
        this.$message({
          type: 'success',
          message: '更新后的余额是: ' + value
        });
        this.newMoney.money = value;
        this.newMoney.userId = row.userId || this.ids;

        update_money(this.newMoney).then(response => {
          this.$modal.msgSuccess("修改成功");
          this.open = false;
          this.getList();
        }).finally(() => {
          this.buttonLoading = false;
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });



    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.form.musicStyle = this.form.musicStyle.join(",");
          if (this.form.userId != null) {
            updatePzc_user(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addPzc_user(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(() => {
        this.loading = true;
        return delPzc_user(userIds);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/pzc_user/export', {
        ...this.queryParams
      }, `pzc_user_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
