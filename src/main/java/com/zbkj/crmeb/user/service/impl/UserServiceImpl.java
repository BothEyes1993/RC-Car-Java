package com.zbkj.crmeb.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.CommonPage;
import com.common.PageParamRequest;
import com.constants.Constants;
import com.constants.SmsConstants;
import com.exception.CrmebException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.utils.CrmebUtil;
import com.utils.DateUtil;
import com.utils.RedisUtil;
import com.utils.vo.dateLimitUtilVo;
import com.zbkj.crmeb.authorization.manager.TokenManager;
import com.zbkj.crmeb.authorization.model.TokenModel;
import com.zbkj.crmeb.finance.request.FundsMonitorSearchRequest;
import com.zbkj.crmeb.front.request.LoginRequest;
import com.zbkj.crmeb.front.request.PasswordRequest;
import com.zbkj.crmeb.front.request.RegisterRequest;
import com.zbkj.crmeb.front.request.UserBindingRequest;
import com.zbkj.crmeb.front.response.*;
import com.zbkj.crmeb.marketing.request.StoreCouponUserSearchRequest;
import com.zbkj.crmeb.marketing.response.StoreCouponUserResponse;
import com.zbkj.crmeb.marketing.service.StoreCouponService;
import com.zbkj.crmeb.marketing.service.StoreCouponUserService;
import com.zbkj.crmeb.store.model.StoreOrder;
import com.zbkj.crmeb.store.request.RetailShopStairUserRequest;
import com.zbkj.crmeb.store.request.StoreOrderSearchRequest;
import com.zbkj.crmeb.store.service.StoreOrderService;
import com.zbkj.crmeb.system.model.SystemUserLevel;
import com.zbkj.crmeb.system.request.SystemUserLevelSearchRequest;
import com.zbkj.crmeb.system.service.SystemConfigService;
import com.zbkj.crmeb.system.service.SystemUserLevelService;
import com.zbkj.crmeb.user.dao.UserDao;
import com.zbkj.crmeb.user.model.User;
import com.zbkj.crmeb.user.model.UserBill;
import com.zbkj.crmeb.user.model.UserLevel;
import com.zbkj.crmeb.user.model.UserSign;
import com.zbkj.crmeb.user.request.RegisterThirdUserRequest;
import com.zbkj.crmeb.user.request.UserOperateFundsRequest;
import com.zbkj.crmeb.user.request.UserOperateIntegralMoneyRequest;
import com.zbkj.crmeb.user.request.UserSearchRequest;
import com.zbkj.crmeb.user.response.TopDetail;
import com.zbkj.crmeb.user.response.UserResponse;
import com.zbkj.crmeb.user.service.*;
import com.zbkj.crmeb.user.vo.UserOperateFundsVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-04-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Resource
    private UserDao userDao;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SystemUserLevelService systemUserLevelService;

    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private UserTagService userTagService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private UserSignService userSignService;

    @Autowired
    private StoreCouponUserService storeCouponUserService;

    @Autowired
    private StoreCouponService storeCouponService;

    /**
     * 分页显示用户表
     * @param request 搜索条件
     * @param pageParamRequest 分页参数
     * @author Mr.Zhang
     * @since 2020-04-10
     * @return List<User>
     */
    @Override
    public PageInfo<UserResponse> getList(UserSearchRequest request, PageParamRequest pageParamRequest) {
        Page<User> pageUser = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if(request.getIsPromoter() != null){
            lambdaQueryWrapper.eq(User::getIsPromoter, request.getIsPromoter());
        }

        if(!StringUtils.isBlank(request.getGroupId())){
            lambdaQueryWrapper.apply(CrmebUtil.getFindInSetSql("group_id", request.getGroupId()));
        }

        if(!StringUtils.isBlank(request.getLabelId())){
            lambdaQueryWrapper.apply(CrmebUtil.getFindInSetSql("tag_id", request.getLabelId()));
        }

        if(!StringUtils.isBlank(request.getLevel())){
            lambdaQueryWrapper.in(User::getLevel, CrmebUtil.stringToArray(request.getLevel()));
        }

        if(StringUtils.isNotBlank(request.getUserType())){
            lambdaQueryWrapper.eq(User::getUserType, request.getUserType());
        }

        if(StringUtils.isNotBlank(request.getSex())){
            lambdaQueryWrapper.eq(User::getSex, request.getSex());
        }

        if(StringUtils.isNotBlank(request.getCountry())){
            lambdaQueryWrapper.eq(User::getCountry, request.getCountry());
        }

        if(request.getStatus() != null){
            lambdaQueryWrapper.eq(User::getStatus, request.getStatus());
        }

        dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getData());

        if(!StringUtils.isBlank(dateLimit.getStartTime())){
            switch (request.getAccessType()){
                case 1:
                    lambdaQueryWrapper.between(User::getUpdateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
                    break;
                case 2:
                    lambdaQueryWrapper.notBetween(User::getUpdateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
                    break;
                case 3:
                    lambdaQueryWrapper.apply(" and create_time = last_login_time");
                    break;
                default:
                    lambdaQueryWrapper.between(User::getCreateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
                    break;
            }
        }
        if(request.getKeywords() != null){
            String keywords = request.getKeywords();
            lambdaQueryWrapper.and(i -> i.or().like(User::getAccount, keywords) //用户账号
                    .or().like(User::getRealName, keywords) //真实姓名
                    .or().like(User::getCardId, keywords) //身份证号码
                    .or().like(User::getMark, keywords) //用户备注
                    .or().like(User::getPhone, keywords) //手机号码
                    .or().like(User::getAddIp, keywords) //创建IP
                    .or().like(User::getLastIp, keywords) //最后一次登录ip
                    .or().like(User::getAddres, keywords) //详细地址
                    .or().like(User::getNickname, keywords)); //用户昵称
        }
        lambdaQueryWrapper.orderByDesc(User::getUid);

        List<User> userList = userDao.selectList(lambdaQueryWrapper);
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : userList) {
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(user, userResponse);
            // 获取分组信息
            if(!StringUtils.isBlank(user.getGroupId())){
                userResponse.setGroupName(userGroupService.getGroupNameInId(user.getGroupId()));
            }

            // 获取标签信息
            if(!StringUtils.isBlank(user.getTagId())){
                userResponse.setTagName(userTagService.getGroupNameInId(user.getTagId()));
            }

            //获取推广人信息
            if(user.getSpreadUid() == 0){
                userResponse.setSpreadNickname("无");
            }else{
                userResponse.setSpreadNickname(userDao.selectById(user.getSpreadUid()).getNickname());
            }
            userResponses.add(userResponse);
        }
        return CommonPage.copyPageInfo(pageUser,userResponses);
    }

    /**
     * 操作资金
     * @author Mr.Zhang
     * @since 2020-04-10
     */
    @Override
    public boolean updateFounds(UserOperateFundsRequest request, boolean isSaveBill) {
        UserOperateFundsVo userOperateFundsVo = new UserOperateFundsVo(request.getUid(), request.getFoundsCategory(), getFounds(request));
        //增加日志， 创建计算数值之前的数据
        if(isSaveBill){
            createUserBill(request, getUserBalance(request));
        }
        Boolean result = userDao.updateFounds(userOperateFundsVo);    //更新数值

        if(request.getFoundsType().equals(Constants.USER_BILL_CATEGORY_EXPERIENCE)){
            //经验升级
            upLevel(request.getUid());
        }

        return result;
    }


    /**
     * 操作资金
     * @author Mr.Zhang
     * @since 2020-04-10
     */
    @Override
    public boolean updateIntegralMoney(UserOperateIntegralMoneyRequest request) {
        if(null == request.getMoneyValue() || null == request.getIntegralValue()){
            throw new CrmebException("至少输入一个金额");
        }

        if(request.getMoneyValue().compareTo(BigDecimal.ZERO) < 1 && request.getIntegralValue().compareTo(BigDecimal.ZERO) < 1){
            throw new CrmebException("最小值为0.01");
        }

        UserOperateFundsRequest userOperateFundsRequest = new UserOperateFundsRequest();
        if(request.getMoneyType() == 1){
            userOperateFundsRequest.setFoundsType(Constants.USER_BILL_TYPE_SYSTEM_ADD);
            userOperateFundsRequest.setType(request.getMoneyType());
        }else{
            userOperateFundsRequest.setFoundsType(Constants.USER_BILL_TYPE_SYSTEM_SUB);
            userOperateFundsRequest.setType(0);
        }

        userOperateFundsRequest.setTitle("后台操作");

        if(request.getIntegralValue().compareTo(BigDecimal.ZERO) > 0){
            //需要处理
            userOperateFundsRequest.setFoundsCategory(Constants.USER_BILL_CATEGORY_INTEGRAL);
            userOperateFundsRequest.setUid(request.getUid());
            userOperateFundsRequest.setValue(request.getIntegralValue());
            updateFounds(userOperateFundsRequest, true);
        }

        if(request.getMoneyValue().compareTo(BigDecimal.ZERO) > 0){
            //需要处理
            userOperateFundsRequest.setFoundsCategory(Constants.USER_BILL_CATEGORY_MONEY);
            userOperateFundsRequest.setUid(request.getUid());
            userOperateFundsRequest.setValue(request.getMoneyValue());
            updateFounds(userOperateFundsRequest, true);
        }

        return true;
    }

    /**
     * 等级升级
     * @param userId Integer 用户id
     * @author Mr.Zhang
     * @since 2020-04-10
     */
    public void upLevel(Integer userId) {
        //确定当前经验所达到的等级
        SystemUserLevelSearchRequest systemUserLevelSearchRequest = new SystemUserLevelSearchRequest();
        systemUserLevelSearchRequest.setIsDel(false);
        systemUserLevelSearchRequest.setIsShow(true);
        List<SystemUserLevel> list = systemUserLevelService.getList(systemUserLevelSearchRequest, new PageParamRequest());

        User user = getInfo();
        SystemUserLevel userLevelConfig = new SystemUserLevel();
        for (SystemUserLevel systemUserLevel : list) {
            if(user.getExperience() > systemUserLevel.getExperience()){
                userLevelConfig = systemUserLevel;
                continue;
            }
            break;
        }

        if(userLevelConfig.getId() == null){
            return;
        }

        //更新用户等级
        userLevelService.level(userId, userLevelConfig.getId());
    }

    /**
     * 更新用户信息
     * @param user 用户参数
     * @return 更新结果
     */
    @Override
    public boolean updateBase(User user) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//        lambdaUpdateWrapper.setEntity(user);
        if(null == user.getUid()) return false;
        lambdaUpdateWrapper.eq(User::getUid,user.getUid());
        if(StringUtils.isNotBlank(user.getNickname())){
            lambdaUpdateWrapper.set(User::getNickname, user.getNickname());
        }
        if(StringUtils.isNotBlank(user.getAccount())){
            lambdaUpdateWrapper.set(User::getAccount, user.getAccount());
        }
        if(StringUtils.isNotBlank(user.getPwd())){
            lambdaUpdateWrapper.set(User::getPwd, user.getPwd());
        }
        if(StringUtils.isNotBlank(user.getRealName())){
            lambdaUpdateWrapper.set(User::getRealName, user.getRealName());
        }
        if(StringUtils.isNotBlank(user.getBirthday())){
            lambdaUpdateWrapper.set(User::getBirthday, user.getBirthday());
        }
        if(StringUtils.isNotBlank(user.getCardId())){
            lambdaUpdateWrapper.set(User::getCardId, user.getCardId());
        }
        if(StringUtils.isNotBlank(user.getMark())){
            lambdaUpdateWrapper.set(User::getMark, user.getMark());
        }
        if(null != user.getPartnerId()){
            lambdaUpdateWrapper.set(User::getPartnerId, user.getPartnerId());
        }
        if(StringUtils.isNotBlank(user.getGroupId())){
            lambdaUpdateWrapper.set(User::getGroupId, user.getGroupId());
        }
        if(StringUtils.isNotBlank(user.getTagId())){
            lambdaUpdateWrapper.set(User::getTagId, user.getTagId());
        }
        if(StringUtils.isNotBlank(user.getAvatar())){
            lambdaUpdateWrapper.set(User::getAvatar, user.getAvatar());
        }
        if(StringUtils.isNotBlank(user.getPhone())){
            lambdaUpdateWrapper.set(User::getPhone, user.getPhone());
        }
        if(StringUtils.isNotBlank(user.getAddIp())){
            lambdaUpdateWrapper.set(User::getAddIp, user.getAddIp());
        }
        if(StringUtils.isNotBlank(user.getLastIp())){
            lambdaUpdateWrapper.set(User::getLastIp, user.getLastIp());
        }
        if(null != user.getNowMoney() && user.getNowMoney().compareTo(BigDecimal.ZERO) > 0){
            lambdaUpdateWrapper.set(User::getNowMoney, user.getNowMoney());
        }
        if(null != user.getBrokeragePrice() && user.getBrokeragePrice().compareTo(BigDecimal.ZERO) > 0){
            lambdaUpdateWrapper.set(User::getBrokeragePrice, user.getBrokeragePrice());
        }
        if(null != user.getIntegral() && user.getIntegral().compareTo(BigDecimal.ZERO) >= 0){
            lambdaUpdateWrapper.set(User::getIntegral, user.getIntegral());
        }
        if(null != user.getExperience() && user.getExperience() > 0){
            lambdaUpdateWrapper.set(User::getExperience, user.getExperience());
        }
        if(null != user.getSignNum() && user.getSignNum() > 0){
            lambdaUpdateWrapper.set(User::getSignNum, user.getSignNum());
        }
        if(null != user.getStatus()){
            lambdaUpdateWrapper.set(User::getStatus, user.getStatus());
        }
        if(null != user.getLevel() && user.getLevel() > 0){
            lambdaUpdateWrapper.set(User::getLevel, user.getLevel());
        }
        if(null != user.getSpreadUid() && user.getSpreadUid() > 0){
            lambdaUpdateWrapper.set(User::getSpreadUid, user.getSpreadUid());
        }
        if(null != user.getSpreadTime()){
            lambdaUpdateWrapper.set(User::getSpreadTime, user.getSpreadTime());
        }
        if(StringUtils.isNotBlank(user.getUserType())){
            lambdaUpdateWrapper.set(User::getUserType, user.getUserType());
        }
        if(null != user.getIsPromoter()){
            lambdaUpdateWrapper.set(User::getIsPromoter, user.getIsPromoter());
        }
        if(null != user.getPayCount()){
            lambdaUpdateWrapper.set(User::getPayCount, user.getPayCount());
        }
        if(null != user.getSpreadCount()){
            lambdaUpdateWrapper.set(User::getSpreadCount, user.getSpreadCount());
        }
        if(StringUtils.isNotBlank(user.getAddres())){
            lambdaUpdateWrapper.set(User::getAddres, user.getAddres());
        }
        return update(lambdaUpdateWrapper);
    }

    @Override
    public void userPayCountPlus(User user) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getUid, user.getUid());
        lambdaUpdateWrapper.set(User::getPayCount, user.getPayCount()+1);
        update(lambdaUpdateWrapper);
    }

    /**
     * 更新用户金额
     * @param userId 用户id
     * @param price 余额
     * @return 更新后的用户对象
     */
    @Override
    public boolean updateNowMoney(int userId, BigDecimal price) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getUid, userId);
        lambdaUpdateWrapper.set(User::getNowMoney, price);
        return update(lambdaUpdateWrapper);
    }

    /**
     * 会员分组
     * @param id String id
     * @param groupIdValue Integer 分组Id
     * @author Mr.Zhang
     * @since 2020-04-28
     */
    @Override
    public boolean group(String id, String groupIdValue) {
        ArrayList<User> userList = new ArrayList<>();
        //循环id处理
        List<User> list = getListInUid(CrmebUtil.stringToArray(id));
        if(list.size() < 1){
            throw new CrmebException("没有找到用户信息");
        }
        for (User user : list) {
            if(!StringUtils.isBlank(user.getGroupId())){
                groupIdValue = user.getGroupId() + groupIdValue;
            }
            //清除已经删除或者去掉的id
            groupIdValue = userGroupService.clean(groupIdValue);
            if(StringUtils.isBlank(groupIdValue)){
                continue;
            }

            List<Integer> groupIdList = CrmebUtil.stringToArray(groupIdValue);
            user.setGroupId("," + StringUtils.join(groupIdList, ",") + ",");
            userList.add(user);
        }
        if(userList.size() < 1){
            throw new CrmebException("没有可供设置的分组");
        }
        return updateBatchById(userList);
    }

    /**
     * 用户id in list
     * @param uidList List<Integer> id
     * @author Mr.Zhang
     * @since 2020-04-28
     */
    private List<User> getListInUid(List<Integer> uidList) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(User::getUid, uidList);
        return userDao.selectList(lambdaQueryWrapper);
    }

    /**
     * 清除会员等级
     * @param userId Integer id
     * @author Mr.Zhang
     * @since 2020-04-28
     */
    @Override
    public boolean cleanLevel(Integer userId) {
        User user = new User();
        user.setUid(userId);
        user.setLevel(0);
        user.setCleanTime(DateUtil.nowDateTimeReturnDate(Constants.DATE_FORMAT));
        return updateById(user);
    }

    /**
     * 手机快速登录, 如果存在则直接登录，不存在注册
     * @param request RegisterRequest 注册数据
     * @param ip String ip
     * @author Mr.Zhang
     * @since 2020-04-29
     */
    @Override
    public LoginResponse register(RegisterRequest request, String ip) {
        //检测验证码
        checkValidateCode(request.getPhone(), request.getValidateCode());

        //删除验证码
        redisUtil.remove(getValidateCodeRedisKey(request.getPhone()));

        //查询手机号信息
        User user = getUserByAccount(request.getPhone());
        if(user == null){
            //此用户不存在，去帮他注册
            request.setPassword("Abc" + CrmebUtil.randomCount(10000, 99999));
            user = saveUser(request, ip);
        }

        //生成token
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token(user));

        //绑定推广关系
        bindSpread(user, request.getSpread());

        loginResponse.setUser(user);
        long time = Constants.TOKEN_EXPRESS_MINUTES * 60;

        loginResponse.setExpiresTime(DateUtil.addSecond(DateUtil.nowDateTime(), (int)time));

        return loginResponse;
    }

    /**
     * 账号密码登录
     * @author Mr.Zhang
     * @since 2020-04-28
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        User user = getUserByAccount(request.getPhone());
        if(user == null){
            throw new CrmebException("此账号未注册");
        }

        if(!user.getStatus()){
            throw new CrmebException("此账号被禁用");
        }

        String password = CrmebUtil.encryptPassword(request.getPassword(), request.getPhone());
        if(!user.getPwd().equals(password)){
            throw new CrmebException("密码错误");
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token(user));
        user.setPwd(null);

        //绑定推广关系
        bindSpread(user, request.getSpreadPid());

        loginResponse.setUser(user);

        return loginResponse;
    }

    /**
     * 修改密码
     * @param request PasswordRequest 密码
     * @author Mr.Zhang
     * @since 2020-04-28
     * @return boolean
     */
    @Override
    public boolean password(PasswordRequest request) {
        //检测验证码
        checkValidateCode(request.getPhone(), request.getValidateCode());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getAccount, request.getPhone());
        User user = userDao.selectOne(lambdaQueryWrapper);

        //密码
        user.setPwd(CrmebUtil.encryptPassword(request.getPassword(), user.getAccount()));
        return update(user, lambdaQueryWrapper);
    }

    /**
     * 退出
     * @param token String token
     * @author Mr.Zhang
     * @since 2020-04-28
     */
    @Override
    public void loginOut(String token) {
        tokenManager.deleteToken(token, Constants.USER_TOKEN_REDIS_KEY_PREFIX);
    }

    /**
     * 获取个人资料
     * @author Mr.Zhang
     * @since 2020-04-28
     * @return User
     */
    @Override
    public User getInfo() {
        if(getUserId() == 0){
            return null;
        }
        return getById(getUserId());
    }

    /**
     * 获取个人资料
     * @author Mr.Zhang
     * @since 2020-04-28
     * @return User
     */
    @Override
    public User getUserPromoter() {
        User user = getInfo();
        if(null == user){
            return null;
        }

        if(!user.getStatus()){
            user.setIsPromoter(false);
            return user;
        }

        //获取分销
        //检测商城是否开启分销功能
        String isOpen = systemConfigService.getValueByKey(Constants.CONFIG_KEY_STORE_BROKERAGE_IS_OPEN);
        if(StringUtils.isBlank(isOpen) || isOpen.equals("0")){
            user.setIsPromoter(false);
        }else{
            String type = systemConfigService.getValueByKey(Constants.CONFIG_KEY_STORE_BROKERAGE_MODEL);
            if(StringUtils.isBlank(type) || type.equals("2")){
                //人人分销
                user.setIsPromoter(true);
            }
        }
        return user;
    }
    /**
     * 获取个人资料
     * @author Mr.Zhang
     * @since 2020-04-28
     * @return User
     */
    @Override
    public User getInfoException() {
        User user = getInfo();
        if(user == null){
            throw new CrmebException("用户信息不存在！");
        }

        if(!user.getStatus()){
            throw new CrmebException("用户已经被禁用！");
        }
        return user;
    }

    @Override
    public User getInfoEmpty() {
        User user = getInfo();
        if(user == null){
            user = new User();
        }

//        if(!user.getStatus()){
//            throw new CrmebException("用户已经被禁用！");
//        }
        return user;
    }

    /**
     * 获取当前用户id
     * @author Mr.Zhang
     * @since 2020-04-28
     * @return Integer
     */
    @Override
    public Integer getUserIdException() {
        return Integer.parseInt(tokenManager.getLocalInfoException("id"));
    }

    /**
     * 获取当前用户id
     * @author Mr.Zhang
     * @since 2020-04-28
     * @return Integer
     */
    @Override
    public Integer getUserId() {
        Object id = tokenManager.getLocalInfo("id");
        if(null == id){
            return 0;
        }
        return Integer.parseInt(id.toString());
    }

    /**
     * 按开始结束时间查询新增用户数量
     * @param date String 时间范围
     * @author Mr.Zhang
     * @since 2020-05-16
     * @return HashMap<String, Object>
     */
    @Override
    public Integer getAddUserCountByDate(String date) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if(StringUtils.isNotBlank(date)){
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(date);
            lambdaQueryWrapper.between(User::getCreateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
        }
        return userDao.selectCount(lambdaQueryWrapper);
    }

    /**
     * 按开始结束时间查询每日新增用户数量
     * @param date String 时间范围
     * @author Mr.Zhang
     * @since 2020-05-16
     * @return HashMap<String, Object>
     */
    @Override
    public Map<Object, Object> getAddUserCountGroupDate(String date) {
        Map<Object, Object> map = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(uid) as uid", "left(create_time, 10) as create_time");
        if(StringUtils.isNotBlank(date)){
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(date);
            queryWrapper.between("create_time", dateLimit.getStartTime(), dateLimit.getEndTime());
        }
        queryWrapper.groupBy("left(create_time, 10)").orderByAsc("create_time");
        List<User> list = userDao.selectList(queryWrapper);
        if(list.size() < 1){
            return map;
        }

        for (User user : list) {
            map.put(DateUtil.dateToStr(user.getCreateTime(), Constants.DATE_FORMAT_DATE), user.getUid());
        }
        return map;
    }

    /**
     * 绑定手机号
     * @author Mr.Zhang
     * @since 2020-04-28
     * @return boolean
     */
    @Override
    public boolean bind(UserBindingRequest request) {
        //检测验证码
        checkValidateCode(request.getPhone(), request.getValidateCode());

        //删除验证码
        redisUtil.remove(getValidateCodeRedisKey(request.getPhone()));

        //检测当前手机号是否已经是账号
        User user = getUserByAccount(request.getPhone());
        if(null != user){
            throw new CrmebException("此手机号码已被注册");
        }

        //查询手机号信息
        User bindUser = getInfoException();
        bindUser.setAccount(request.getPhone());
        bindUser.setPhone(request.getPhone());

        return updateById(bindUser);
    }

    /**
     * 用户中心
     * @author Mr.Zhang
     * @since 2020-04-28
     * @return UserCenterResponse
     */
    @Override
    public UserCenterResponse getUserCenter() {
        UserCenterResponse userCenterResponse = new UserCenterResponse();
        User currentUser = getInfo();
        BeanUtils.copyProperties(currentUser, userCenterResponse);
        userCenterResponse.setIntegral(currentUser.getIntegral().intValue());
        UserCenterOrderStatusNumResponse userCenterOrderStatusNumResponse = new UserCenterOrderStatusNumResponse();
        userCenterOrderStatusNumResponse.setNoBuy(0);
        userCenterOrderStatusNumResponse.setNoPink(0);
        userCenterOrderStatusNumResponse.setNoPostage(0);
        userCenterOrderStatusNumResponse.setNoRefund(0);
        userCenterOrderStatusNumResponse.setNoReply(0);
        userCenterOrderStatusNumResponse.setNoTake(0);
        userCenterResponse.setOrderStatusNum(userCenterOrderStatusNumResponse);
        PageParamRequest pageParamRequest = new PageParamRequest();
        pageParamRequest.setPage(1); pageParamRequest.setLimit(999);
        List<StoreCouponUserResponse> storeCoupons = storeCouponUserService.getListFront(getUserIdException(), pageParamRequest);
        userCenterResponse.setCouponCount(null != storeCoupons ? storeCoupons.size():0);
        userCenterResponse.setLevel(currentUser.getLevel());

        // 判断是否开启会员功能
        Integer memberFuncStatus = Integer.valueOf(systemConfigService.getValueByKey("member_func_status"));
        if(memberFuncStatus == 0){
            userCenterResponse.setVip(false);
        }else{
            userCenterResponse.setVip(userCenterResponse.getLevel() > 0);
            UserLevel userLevel = userLevelService.getUserLevelByUserId(currentUser.getUid());
            if(null != userLevel){
                SystemUserLevel systemUserLevel = systemUserLevelService.getByLevelId(userLevel.getLevelId());
                userCenterResponse.setVipIcon(systemUserLevel.getIcon());
                userCenterResponse.setVipName(systemUserLevel.getName());
            }
        }
        return userCenterResponse;
    }

    /**
     * 根据用户id获取用户列表 map模式
     * @author Mr.Zhang
     * @since 2020-04-28
     * @return HashMap<Integer, User>
     */
    @Override
    public HashMap<Integer, User> getMapListInUid(List<Integer> uidList) {
        List<User> userList = getListInUid(uidList);
        return getMapByList(userList);
    }

    /**
     * 根据用户id获取用户列表 map模式
     * @author Mr.Zhang
     * @since 2020-04-28
     * @return HashMap<Integer, User>
     */
    @Override
    public HashMap<Integer, User> getMapByList(List<User> list) {
        HashMap<Integer, User> map = new HashMap<>();
        if(null == list || list.size() < 1){
            return map;
        }

        for (User user: list) {
            map.put(user.getUid(), user);
        }

        return map;
    }

    /**
     * 重置连续签到天数
     * @param userId Integer 用户id
     * @author Mr.Zhang
     * @since 2020-04-28
     */
    @Override
    public void repeatSignNum(Integer userId) {
        User user = new User();
        user.setUid(userId);
        user.setSignNum(0);
        updateById(user);
    }

    /**
     * 推广数据接口(昨天的佣金 累计提现金额 当前佣金)
     * @author Mr.Zhang
     * @since 2020-06-08
     */
    @Override
    public UserCommissionResponse getCommission() {
        UserCommissionResponse userCommissionResponse = new UserCommissionResponse();
        //昨天的佣金
        userCommissionResponse.setLastDayCount(userBillService.getSumBigDecimal(0, getUserId(), Constants.USER_BILL_CATEGORY_MONEY, Constants.SEARCH_DATE_YESTERDAY, null));
        userCommissionResponse.setExtractCount(userBillService.getSumBigDecimal(0, getUserId(), Constants.USER_BILL_CATEGORY_MONEY, null, Constants.USER_BILL_TYPE_EXTRACT));
        userCommissionResponse.setCommissionCount(getInfo().getBrokeragePrice());
        return userCommissionResponse;
    }

    /**
     * 会员标签
     * @param id String id
     * @param tagIdValue Integer 标签Id
     * @author Mr.Zhang
     * @since 2020-04-28
     */
    @Override
    public boolean tag(String id, String tagIdValue) {
        //循环id处理
        ArrayList<User> userList = new ArrayList<>();
        //循环id处理
        List<User> list = getListInUid(CrmebUtil.stringToArray(id));
        if(list.size() < 1){
            throw new CrmebException("没有找到用户信息");
        }
        for (User user : list) {
            if(!StringUtils.isBlank(user.getTagId())){
                tagIdValue = user.getTagId() + tagIdValue;
            }
            //清除已经删除或者去掉的id
            tagIdValue = userTagService.clean(tagIdValue);
            if(StringUtils.isBlank(tagIdValue)){
                continue;
            }

            List<Integer> tagIdList = CrmebUtil.stringToArray(tagIdValue);
            user.setTagId("," + StringUtils.join(tagIdList, ",") + ",");
            userList.add(user);
        }
        if(userList.size() < 1){
            throw new CrmebException("没有可供设置的标签");
        }
        return updateBatchById(userList);
    }

    /**
     * 根据用户id获取自己本身的推广用户
     * @param userIdList List<Integer> 用户id集合
     * @author Mr.Zhang
     * @since 2020-05-18
     * @return List<User>
     */
    @Override
    public List<Integer> getSpreadPeopleIdList(List<Integer> userIdList) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(User::getUid); //查询用户id
        lambdaQueryWrapper.in(User::getSpreadUid, userIdList); //xx的下线集合
        List<User> list = userDao.selectList(lambdaQueryWrapper);

        if(null == list || list.size() < 1){
            return new ArrayList<>();
        }
        return list.stream().map(User::getUid).distinct().collect(Collectors.toList());
    }

    /**
     * 根据用户id获取自己本身的推广用户
     * @author Mr.Zhang
     * @since 2020-05-18
     * @return List<User>
     */
    @Override
   public List<UserSpreadPeopleItemResponse> getSpreadPeopleList(
            List<Integer> userIdList, String keywords, String sortKey, String isAsc, PageParamRequest pageParamRequest) {

        PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        Map<String, Object> map = new HashMap<>();
        map.put("userIdList", userIdList.stream().map(String::valueOf).distinct().collect(Collectors.joining(",")));
        if(StringUtils.isNotBlank(keywords)){
            map.put("keywords", "%"+keywords+"%");
        }
        map.put("sortKey", "create_time");
        if(StringUtils.isNotBlank(sortKey)){
            map.put("sortKey", sortKey);
        }
        map.put("sortValue", Constants.SORT_DESC);
        if(isAsc.toLowerCase().equals(Constants.SORT_ASC)){
            map.put("sortValue", Constants.SORT_ASC);
        }

        return userDao.getSpreadPeopleList(map);
    }



    /**
     * 登录用户生成token
     * @author Mr.Zhang
     * @since 2020-04-29
     */
    @Override
    public String token(User user) {
        TokenModel token = tokenManager.createToken(user.getAccount(), user.getUid().toString(), Constants.USER_TOKEN_REDIS_KEY_PREFIX);
        return token.getToken();
    }

    /**
     * 新增一个用户
     * @param request RegisterRequest 注册信息
     * @param ip String ip
     * @author Mr.Zhang
     * @since 2020-04-29
     * @return User
     */
    private User saveUser(RegisterRequest request, String ip) {
        //检测推广人信息
        User user = new User();
        if(request.getSpread() != null && request.getSpread() > 0){

            User spread = getById(request.getSpread());
            if(spread != null){
                user.setSpreadUid(request.getSpread());
                user.setSpreadTime(DateUtil.nowDateTimeReturnDate(Constants.DATE_FORMAT));
            }
        }


        user.setAccount(request.getPhone());
        user.setPwd(CrmebUtil.encryptPassword(request.getPassword(), request.getPhone()));
        user.setPhone(request.getPhone());
        user.setUserType(Constants.USER_LOGIN_TYPE_H5);
        user.setAddIp(ip);
        user.setLastIp(ip);
        user.setNickname(
                DigestUtils.md5Hex(request.getPhone() + DateUtil.getNowTime()).
                        subSequence(0, 12).
                        toString()
        );
        user.setAvatar(systemConfigService.getValueByKey(Constants.USER_DEFAULT_AVATAR_CONFIG_KEY));
        save(user);

        // 添加推广关系
        if(request.getSpread() > 0){
            spread(user.getUid(),request.getSpread());
        }
        return user;
    }

    /**
     * 检测手机验证码
     * @author Mr.Zhang
     * @since 2020-04-29
     */
    private void checkValidateCode(String phone, String value) {
        Object validateCode = redisUtil.get(getValidateCodeRedisKey(phone));
        if(validateCode == null){
            throw new CrmebException("验证码已过期");
        }

        if(!validateCode.toString().equals(value)){
            throw new CrmebException("验证码错误");
        }
    }

    /**
     * 检测手机验证码
     * @param phone String 手机号
     * @author Mr.Zhang
     * @since 2020-04-29
     * @return String
     */
    @Override
    public String getValidateCodeRedisKey(String phone) {
        return SmsConstants.SMS_VALIDATE_PHONE + phone;
    }

    /**
     * 通过账号获取用户信息
     * @author Mr.Zhang
     * @since 2020-04-29
     */
    private User getUserByAccount(String account) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getAccount, account);
        return userDao.selectOne(lambdaQueryWrapper);
    }

    /**
     * 日志title
     * @author Mr.Zhang
     * @since 2020-04-10
     * @return String
     */
    private String getTitle(UserOperateFundsRequest request) {
        String operate = (request.getType() == 1) ? "增加" : "减少";
        String founds = "";
        switch (request.getFoundsType()){
            case Constants.USER_BILL_CATEGORY_INTEGRAL:
                founds = "积分";
                break;
            case Constants.USER_BILL_CATEGORY_MONEY:
                founds = "余额";
                break;
            case Constants.USER_BILL_CATEGORY_EXPERIENCE:
                founds = "经验";
                break;
        }

        return Constants.USER_BILL_OPERATE_LOG_TITLE.replace("{$title}", request.getTitle()).replace("{$operate}", operate).replace("{$founds}", founds);
    }


    /**
     * 资金操作日志增加
     * @author Mr.Zhang
     * @since 2020-04-10
     */
    private void createUserBill(UserOperateFundsRequest request, BigDecimal balance) {
        UserBill userBill = new UserBill();
        userBill.setTitle(request.getTitle());
        userBill.setUid(request.getUid());
        userBill.setCategory(request.getFoundsCategory());
        userBill.setType(request.getFoundsType());
        userBill.setNumber(request.getValue());
        userBill.setLinkId(request.getLinkId());  //链接id
        userBill.setMark(getTitle(request).replace("{$value}", request.getValue().toString()));
        userBill.setPm(request.getType());
        userBill.setBalance(balance.add(request.getValue()));
        userBillService.save(userBill);
    }

    /**
     * 操作资金
     * @author Mr.Zhang
     * @since 2020-04-10
     */
    private String getFounds(UserOperateFundsRequest request) {
        if(request.getType() == 1){
            return " +" + request.getValue();
        }
        if(request.getType() == 0){
            checkFounds(request);
            return " -" + request.getValue();
        }

        return " +0.00";
    }

    /**
     * 操作资金
     * @author Mr.Zhang
     * @since 2020-04-10
     */
    private void checkFounds(UserOperateFundsRequest request) {
        BigDecimal value = getUserBalance(request);
        if(value == null){
            throw new CrmebException("请选择正确的操作类型");
        }

        int result = value.subtract(request.getValue()).compareTo(BigDecimal.ZERO);
        if(result < 0){
            throw new CrmebException("此用户当前资金为 " + value + "， 需要减少的资金不能大于 " + value);
        }
    }

    /**
     * 获取用户资金
     * @author Mr.Zhang
     * @since 2020-04-10
     */
    private BigDecimal getUserBalance(UserOperateFundsRequest request) {
        //获取用户信息
        User user = getById(request.getUid());

        BigDecimal value = null;
        if(request.getFoundsCategory().equals(Constants.USER_BILL_CATEGORY_INTEGRAL)){
            value = user.getIntegral();
        }

        if(request.getFoundsCategory().equals(Constants.USER_BILL_CATEGORY_MONEY)){
            value = user.getNowMoney();
        }

        if(request.getFoundsCategory().equals(Constants.USER_BILL_CATEGORY_EXPERIENCE)){
            value = new BigDecimal(user.getExperience());
        }

        if(request.getFoundsCategory().equals(Constants.USER_BILL_CATEGORY_BROKERAGE_PRICE)){
            value = user.getBrokeragePrice();
        }


        return value;
    }

    /**
     * 获取用户好友关系，spread_uid往下两级的用户信息
     * @return
     */
    private List<User> getUserRelation(Integer userId){
        List<User> userList = new ArrayList<>();
        User currUser = userDao.selectById(userId);
        if(currUser.getSpreadUid() > 0){
            User spUser1 = userDao.selectById(currUser.getSpreadUid());
            if(null != spUser1){
                userList.add(spUser1);
                if(spUser1.getSpreadUid() > 0){
                    User spUser2 = userDao.selectById(spUser1.getSpreadUid());
                    if(null != spUser2){
                        userList.add(spUser2);
                    }
                }
            }
        }
        return userList;
    }

    /**
     * 根据条件获取会员对应信息列表
     * @param userId
     * @param type 0=消费记录，1=积分明细，2=签到记录，3=持有优惠券，4=余额变动，5=好友关系
     * @param pageParamRequest
     * @return
     */
    @Override
    public Object getInfoByCondition(Integer userId, Integer type, PageParamRequest pageParamRequest) {
        switch (type){
            case 0:
                StoreOrderSearchRequest sor = new StoreOrderSearchRequest();
                sor.setUid(userId);
                return storeOrderService.getList(sor, pageParamRequest);
            case 1:
                FundsMonitorSearchRequest fmsq = new FundsMonitorSearchRequest();
                fmsq.setUid(userId);
                fmsq.setCategory(Constants.USER_BILL_CATEGORY_INTEGRAL);
                return userBillService.getList(fmsq,pageParamRequest);
            case 2:
                UserSign userSign = new UserSign();
                userSign.setUid(userId);
                return userSignService.getListByCondition(userSign,pageParamRequest);
            case 3:
                StoreCouponUserSearchRequest scur = new StoreCouponUserSearchRequest();
                scur.setUid(userId);
                PageInfo<StoreCouponUserResponse> pageInfo = storeCouponUserService.getList(scur,pageParamRequest);
                if(null == pageInfo.getList() || pageInfo.getList().size() < 1){
                    return new ArrayList<>();
                }
                return pageInfo.getList();
            case 4:
                FundsMonitorSearchRequest fmsqq = new FundsMonitorSearchRequest();
                fmsqq.setUid(userId);
                return userBillService.getList(fmsqq,pageParamRequest);
            case 5:
                return getUserRelation(userId);
        }

        return new ArrayList<>();
    }

    /**
     * 会员详情顶部数据
     * @param userId Integer 用户id
     * @return Object
     */
    @Override
    public TopDetail getTopDetail(Integer userId) {
        TopDetail topDetail = new TopDetail();
        User currentUser = userDao.selectById(userId);
        topDetail.setUser(currentUser);
        topDetail.setBalance(currentUser.getNowMoney());
        topDetail.setIntegralCount(currentUser.getIntegral());
        topDetail.setMothConsumeCount(storeOrderService.getSumBigDecimal(userId, Constants.SEARCH_DATE_MONTH));
        topDetail.setAllConsumeCount(storeOrderService.getSumBigDecimal(userId, null));
        topDetail.setMothOrderCount(storeOrderService.getOrderCount(userId, Constants.SEARCH_DATE_MONTH));
        topDetail.setAllOrderCount(storeOrderService.getOrderCount(userId, null));
        return topDetail;
    }

    /**
     * 通过微信信息注册用户
     * @param thirdUserRequest RegisterThirdUser 三方用户登录注册信息
     * @return User
     */
    @Override
    public User registerByThird(RegisterThirdUserRequest thirdUserRequest, String type) {
        User user = new User();
        user.setAccount(DigestUtils.md5Hex(CrmebUtil.getUuid() + DateUtil.getNowTime()));
        user.setUserType(type);
        user.setNickname(thirdUserRequest.getNickName());
        user.setAvatar(thirdUserRequest.getAvatar());
        user.setSpreadUid(thirdUserRequest.getSpreadPid());
        user.setSpreadTime(DateUtil.nowDateTime());
        user.setSex(Integer.parseInt(thirdUserRequest.getSex()));
        user.setAddres(thirdUserRequest.getCountry() + "," + thirdUserRequest.getProvince() + "," + thirdUserRequest.getCity());
        save(user);
        return user;
    }

     /** 根据用户id集合获取对应用户集合
     * @param userIds 用户id集合
     * @return 用户集合
     */
    @Override
    public List<User> getSpreadPeopleList(List<Integer> userIds) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(User::getUid, userIds);
        List<User> userList = userDao.selectList(lambdaQueryWrapper);
        return userList;
    }

    /**
     * 添加推广关系
     * @param currentUserId 当前用户id 被推广人
     * @param spreadUserId 推广人id
     * @return 添加推广关系是否成功
     */
    @Override
    public boolean spread(Integer currentUserId, Integer spreadUserId) {
        // 检查用户是否存在
        User currentUser = userDao.selectById(currentUserId);
        if(null == currentUser) throw new CrmebException("用户id:"+currentUserId+"不存在");
        User spreadUser = userDao.selectById(spreadUserId);
        if(null == spreadUser) throw new CrmebException("用户id:"+spreadUserId+"不存在");
        // 检查是否是推广员
        if(!spreadUser.getIsPromoter()) throw new CrmebException("用户id:"+spreadUserId+"不是推广员身份");
        // 检查是否已经有推广关系
        LambdaQueryWrapper<User> lmq = new LambdaQueryWrapper<>();
        lmq.like(User::getPath,spreadUserId);
        lmq.eq(User::getUid, currentUserId);
        List<User> spreadUsers = userDao.selectList(lmq);
        if(spreadUsers.size() > 0){
            throw new CrmebException("推广关系已经存在");
        }
        currentUser.setPath(currentUser.getPath()+spreadUser.getUid()+"/");
        currentUser.setSpreadUid(spreadUserId);
        currentUser.setSpreadTime(new Date());
        currentUser.setSpreadCount(currentUser.getSpreadCount()+1);
        return userDao.updateById(currentUser) >= 0;
    }

    /**
     * 根据推广级别和其他参数当前用户下的推广列表
     * @param request 推广列表参数
     * @return 当前用户的推广人列表
     */
    @Override
    public PageInfo<User> getUserListBySpreadLevel(RetailShopStairUserRequest request, PageParamRequest pageParamRequest) {
        Page<User> userPage = PageHelper.startPage(pageParamRequest.getPage(),pageParamRequest.getLimit());
        List<User> users = getUsersBySpreadLevel(request);
        return CommonPage.copyPageInfo(userPage, users);
    }

    /**
     * 根据推广级别和其他参数获取推广列表
     * @param request 推广层级和推广时间参数
     * @return 推广订单列表
     */
    @Override
    public PageInfo<StoreOrder> getOrderListBySpreadLevel(RetailShopStairUserRequest request, PageParamRequest pageParamRequest) {
        List<User> users = getUsersBySpreadLevel(request);
        if(users.size() == 0){
            return new PageInfo<>();
        }
        List<Integer> userIds = users.stream().map(User::getUid).distinct().collect(Collectors.toList());
        Page<StoreOrder> storeOrderPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        return CommonPage.copyPageInfo(storeOrderPage, storeOrderService.getOrderByUserIdsForRetailShop(userIds));
    }

    /**
     * 根据用户id清除用户当前推广人
     * @param userId 当前推广人id
     * @return 清除推广结果
     */
    @Override
    public boolean clearSpread(Integer userId) {
        User user = new User();
        user.setUid(userId);
        user.setPath("/0/");
        user.setSpreadUid(0);
        user.setSpreadTime(null);
        return userDao.updateById(user) > 0;
    }

    /**
     * 公用 根据其他参数和推广等级获取对象列表
     * @param request 查询参数
     * @return 用户列表
     */
    private List<User> getUsersBySpreadLevel(RetailShopStairUserRequest request) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(User::getUid,request.getUid());
//        lqw.like(User::getPath, request.getUid());
        lqw.eq(User::getSpreadUid, request.getUid());
        if(StringUtils.isNotBlank(request.getDateLimit())){
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());
            lqw.between(User::getCreateTime,dateLimit.getStartTime(),dateLimit.getEndTime());
        }
        if(StringUtils.isNotBlank(request.getNickName())){
            lqw.like(User::getNickname, request.getNickName())
                    .or().eq(User::getPhone, request.getNickName());
        }
        String type ;
        if(null == request.getType()){
            type = ">= 3";
        }else {
            type = request.getType() == 1 ? "= 3":"= 4";
        }
        // path长度 减去 将/字符替换掉的长度 = 等级 2=没有 3=一级推广人 4= 二级推广人
        lqw.last("and (LENGTH(path) - LENGTH(REPLACE(path,'/','')))" + type);
        return userDao.selectList(lqw);
    }

    /**
     * 根据条件是否升级为推广人
     * @param current 当前用户
     * @return 是否推广人
     */
    @Override
    public User updateForPromoter(User current) {
        if(current.getIsPromoter()){
            return current;
        }
        LambdaQueryWrapper<StoreOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StoreOrder::getPaid, 1);
        lqw.eq(StoreOrder::getRefundStatus, 0);
        lqw.eq(StoreOrder::getUid, current.getUid());
        List<StoreOrder> orders = storeOrderService.list(lqw);
        BigDecimal price = orders.stream().map(e->e.getPayPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 判断是否能成为推广人
        boolean isPromoter = checkIsPromoter(price);
        if(isPromoter) {
            current.setIsPromoter(true);
            int updateCount = userDao.updateById(current);
            if(updateCount <= 0) throw new CrmebException("升级推广人时 更新用户信息出错");
            return current;
        }
        return current;
    }

    /**
     * 推荐人反拥条件是否成立
     * @param price 当前价格
     * @return 是否可反拥
     */
    @Override
    public boolean checkIsPromoter(BigDecimal price) {
        String brokerageStatus = systemConfigService.getValueByKey(Constants.CONFIG_KEY_STORE_BROKERAGE_IS_OPEN);
        if(brokerageStatus.equals("1")){
            return false;
        }else{
            BigDecimal  brokeragePrice = BigDecimal.valueOf(Integer.parseInt(systemConfigService.getValueByKey("store_brokerage_price")));
            return price.compareTo(brokeragePrice) == 1;
        }
    }

    /**
     * 推广人排行
     * @param type String 类型
     * @param pageParamRequest PageParamRequest 分页
     * @return List<User>
     */
    @Override
    public List<User> getTopSpreadPeopleListByDate(String type, PageParamRequest pageParamRequest) {
        PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        dateLimitUtilVo dateLimit = DateUtil.getDateLimit(type);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(spread_count) as spread_count, spread_uid")
                .gt("spread_uid", 0)
                .eq("status", true);
        if(!StringUtils.isBlank(dateLimit.getStartTime())){
            queryWrapper.between("create_time", dateLimit.getStartTime(), dateLimit.getEndTime());
        }
        queryWrapper.groupBy("spread_uid").orderByDesc("spread_count");
        List<User> spreadVoList = userDao.selectList(queryWrapper);
        if(spreadVoList.size() < 1){
            return null;
        }

        List<Integer> spreadIdList = spreadVoList.stream().map(User::getSpreadUid).collect(Collectors.toList());
        if(spreadIdList.size() < 1){
            return null;
        }

        ArrayList<User> userList = new ArrayList<>();
        //查询用户
        HashMap<Integer, User> userVoList = getMapListInUid(spreadIdList);

        //解决排序问题
        for (User spreadVo: spreadVoList) {
            User user = new User();
            User userVo = userVoList.get(spreadVo.getSpreadUid());
            user.setUid(spreadVo.getSpreadUid());
            user.setAvatar(userVo.getAvatar());
            user.setSpreadCount(userVo.getSpreadCount());
            if(StringUtils.isBlank(userVo.getNickname())){
                user.setNickname(userVo.getPhone().substring(0, 2) + "****" + userVo.getPhone().substring(7));
            }else{
                user.setNickname(userVo.getNickname());
            }

            userList.add(user);
        }

        return userList;
    }

    /**
     * 推广人排行
     * @param minPayCount int 最小消费次数
     * @param maxPayCount int 最大消费次数
     * @return Integer
     */
    @Override
    public Integer getCountByPayCount(int minPayCount, int maxPayCount) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.between(User::getPayCount, minPayCount, maxPayCount);
        return userDao.selectCount(lambdaQueryWrapper);
    }

    @Override
    public List<User> getUserByEntity(User user) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.setEntity(user);
        return userDao.selectList(lambdaUpdateWrapper);
    }

    /**
     * 消费金钱之后增加经验和积分
     * @param uid Integer 用户id
     * @param price BigDecimal 实际支付金额
     * @return void
     */
    @Override
    public void consumeAfterUpdateUserFounds(Integer uid, BigDecimal price, String type) {
        //赠送积分比例
        String integralStr = systemConfigService.getValueByKey(Constants.CONFIG_KEY_INTEGRAL_RATE);
        BigDecimal integral = new BigDecimal(integralStr);

        //更新用户积分信息
        UserOperateFundsRequest founds = new UserOperateFundsRequest();
        founds.setFoundsType(type);
        founds.setTitle(Constants.ORDER_LOG_MESSAGE_PAY_SUCCESS);

        founds.setUid(uid);
        founds.setFoundsCategory(Constants.USER_BILL_CATEGORY_INTEGRAL);
        founds.setType(1);

        //参考 CrmebUtil getRate说明
        founds.setValue(integral.multiply(price).setScale(0, BigDecimal.ROUND_DOWN));
        updateFounds(founds, true);

        //更新用户经验信息
        founds.setUid(uid);
        founds.setFoundsCategory(Constants.USER_BILL_CATEGORY_EXPERIENCE);
        founds.setType(1);
        founds.setValue(price.setScale(0, BigDecimal.ROUND_DOWN));
        updateFounds(founds, true);
    }

    /**
     * 绑定分销关系
     * @param user User 用户user类
     * @param spreadUid Integer 推广人id
     * @return void
     */
    @Override
    public void bindSpread(User user, Integer spreadUid) {
        //新用户会在注册的时候单独绑定，此处只处理登录用户
        if(null == spreadUid || spreadUid == 0){
            return;
        }
        //如果当前用户没有绑定，并且后台配置人人分销，那么需要邦迪
        if(user.getSpreadUid() > 0){
            return;
        }

        String distribution = systemConfigService.getValueByKey(Constants.CONFIG_KEY_DISTRIBUTION_TYPE);

        //人人分销 + 当前已绑定的id和推广人id不一样
        if(distribution.equals("0") && !user.getSpreadUid().equals(spreadUid)){
            user.setSpreadUid(spreadUid);
            user.setSpreadTime(DateUtil.nowDateTime());
        }
        updateById(user);
    }
}
