package com.ttice.icewkment.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ttice.icewkment.commin.vo.ArticlePageVO;
import com.ttice.icewkment.commin.vo.ArticleVO;
import com.ttice.icewkment.commin.vo.SquareVO;
import com.ttice.icewkment.entity.Article;
import com.ttice.icewkment.entity.ArticleClass;
import com.ttice.icewkment.entity.User;
import com.ttice.icewkment.mapper.ArticleClassMapper;
import com.ttice.icewkment.mapper.ArticleMapper;
import com.ttice.icewkment.mapper.ArticleVOMapper;
import com.ttice.icewkment.mapper.UserMapper;
import com.ttice.icewkment.service.ArticleCommentService;
import com.ttice.icewkment.service.ArticleService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-02-19
 */
@io.swagger.annotations.Api(tags = "Web文章接口")
@RestController
@RequestMapping("/WebArticle")
public class WebArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleVOMapper articleVOMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleCommentService articleCommentService;
    @Autowired
    private ArticleClassMapper articleClassMapper;

    @ApiOperation(value = "根据id获取文章内容")
    @ApiImplicitParam(name = "id",value = "文章id",required = true)
    @GetMapping("/getArticleById/{id}")
    public Article getArticleById(
            @PathVariable("id") Integer id
    ) {
        return this.articleService.getById(id);
    }

    @ApiOperation(value = "获取全部文章列表(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页数",required = true),
            @ApiImplicitParam(name = "limit",value = "总量",required = true)
    })
    @GetMapping("/getAllArticle/{page}/{limit}")
    public ArticlePageVO getAllArticle(
            @PathVariable("page") Integer page,
            @PathVariable("limit") Integer limit
    ) {
        return this.articleService.VoList(page, limit);
    }

    @ApiOperation(value = "获取最新文章列表")
    @ApiImplicitParam(name = "articleNum",value = "数量",required = true)
    @GetMapping("/getNewArticle/{articleNum}")
    public List<ArticleVO> getNewAllArticle(
            @PathVariable("articleNum") Integer articleNum
    ) {

        return articleVOMapper.selectAll(articleNum);
    }

    @ApiOperation(value = "获取所有文章数量")
    @GetMapping("/getAllArticleNumber")
    public Integer getAllArticleNumber() {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.select().eq("status","published");
        return articleVOMapper.selectCount(wrapper);
    }

    @ApiOperation(value = "统计文章浏览量+1")
    @ApiImplicitParam(name = "id",value = "文章id",required = true)
    @GetMapping("/articles/{id}/view")
    public Boolean articlesViewBrowse(
        @PathVariable("id") Integer id
    ) {
        return articleMapper.articlesBrowse(id);
    }

    @ApiOperation(value = "统计文章喜欢量+1")
    @ApiImplicitParam(name = "id",value = "文章id",required = true)
    @GetMapping("/articles/{id}/love")
    public Boolean articlesLoveBrowse(
            @PathVariable("id") Integer id
    ) {
        return articleMapper.articlesLoveBrowse(id);
    }

    @ApiOperation(value = "文章查询(全部)")
    @ApiImplicitParam(name = "content",value = "模糊查询标题",required = true)
    @GetMapping("/findarticles/{content}")
    public List<Article> Findarticles(
            @PathVariable("content") String content
    ) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.like("title",content);
        return articleMapper.selectList(wrapper);
    }

    @ApiOperation(value = "文章查询(预览)")
    @ApiImplicitParam(name = "content",value = "模糊查询标题",required = true)
    @GetMapping("/findarticlesbynum/{content}/{num}")
    public List<Article> FindarticlesByNum(
            @PathVariable("content") String content,
            @PathVariable("num") String num
    ) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.like("title",content)
                .last("limit "+num);
        return articleMapper.selectList(wrapper);
    }

    @ApiOperation(value = "查询文章(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content",value = "内容",required = true),
            @ApiImplicitParam(name = "page",value = "页数",required = true),
            @ApiImplicitParam(name = "limit",value = "总量",required = true)
    })
    @GetMapping("/FindAllArticle/{content}/{page}/{limit}")
    public ArticlePageVO FindAllArticle(
            @PathVariable("content") String content,
            @PathVariable("page") Integer page,
            @PathVariable("limit") Integer limit
    ) {
        return this.articleService.FindVoList(page, limit , content);
    }

    @ApiOperation(value = "根据作者name查询对应头像")
    @ApiImplicitParam(name = "name",value = "作者名称",required = true)
    @GetMapping("/FindProfileByName/{name}")
    public String FindProfileByName(
            @PathVariable("name") String name
    ) {
        QueryWrapper<User> Wrapper = new QueryWrapper<>();
        Wrapper.eq("USERNAME",name);
        User user = this.userMapper.selectOne(Wrapper);
        return user.getProfile();
    }

    @ApiOperation(value = "获取重要文章")
    @GetMapping("/GetArticleBtmatter")
    public List<ArticleVO> GetArticleBtmatter(
    ) {
        List<ArticleVO> result = new ArrayList<>();

        QueryWrapper<Article> Wrapper = new QueryWrapper<>();
        Wrapper.select().orderByAsc("add_time");
        Wrapper.orderByDesc("owner_tag");
        Wrapper.last("limit 0,4");
        List<Article> articles = this.articleMapper.selectList(Wrapper);
        for (Article article : articles) {
            //获取id
            Integer aid = article.getId();
            //获取对应评论数量
            int acnum = articleCommentService.GetCommentNum(aid);
            //根据作者名称查询对应的头像地址
            String author = article.getAuthor();
            User users = userMapper.searchName(author);
            String profile = users.getProfile();
            //获取对应分类
            String sortClass = article.getSortClass();
            ArticleClass articleClass = articleClassMapper.selectById(sortClass);
            String classname = articleClass.getName();

            ArticleVO articleVOs = new ArticleVO();
            articleVOs.setProfile(profile);
            articleVOs.setCommentNum(acnum);
            articleVOs.setClassName(classname);

            BeanUtils.copyProperties(article,articleVOs);
            result.add(articleVOs);
        }
        return result;


    }
}

