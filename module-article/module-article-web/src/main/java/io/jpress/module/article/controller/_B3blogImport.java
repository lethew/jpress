package io.jpress.module.article.controller;

import com.jfinal.aop.Inject;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jpress.JPressConsts;
import io.jpress.module.article.model.Article;
import io.jpress.module.article.model.ArticleCategory;
import io.jpress.module.article.service.ArticleCategoryService;
import io.jpress.module.article.service.ArticleService;
import io.jpress.web.base.AdminControllerBase;

import java.sql.*;
import java.util.List;

@RequestMapping(value = "/admin/setting/tools/b3blog", viewPath = JPressConsts.DEFAULT_ADMIN_VIEW)
public class _B3blogImport  extends AdminControllerBase {
    @Inject
    private ArticleService articleService;
    @Inject
    private ArticleCategoryService articleCategoryService;

    public void index() {
        render("article/b3blog.html");
    }

    public void doImport()throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/b3blog?useUnicode=true&characterEncoding=UTF-8","root","123456");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from b3_solo_article");
        while (resultSet.next()) {
            Article article = new Article();
            article.setTitle(resultSet.getString("articleTitle"));
            article.setContent(resultSet.getString("articleContent"));
            article.setCreated(resultSet.getDate("articleCreateDate"));
            article.setModified(resultSet.getDate("articleUpdateDate"));
            article.setEditMode(JPressConsts.EDIT_MODE_MARKDOWN);
            article.setStatus(Article.STATUS_NORMAL);
            article.setUserId(getLoginedUser().getId());
            articleService.save(article);

            String[] categoryNames = resultSet.getString("articleTags").split(",");
            List<ArticleCategory> categoryList = articleCategoryService.doNewOrFindByTagString(categoryNames);
            Long[] allIds = new Long[categoryList.size()];
            for (int i = 0; i < allIds.length; i++) {
                allIds[i] = categoryList.get(i).getId();
            }
            articleService.doUpdateCategorys(article.getId(), allIds);


        }
        connection.close();

        renderOkJson();
    }
}
