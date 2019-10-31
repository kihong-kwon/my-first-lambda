package net.kkhstudy.myfirstlambda;

import net.kkhstudy.myfirstlambda.dao.TestDao;
import net.kkhstudy.myfirstlambda.data.DemoRequest;
import net.kkhstudy.myfirstlambda.dynamodb.query.DynamoDBQueryCreator;
import net.kkhstudy.myfirstlambda.entity.TitleAuthorEntity;
import net.kkhstudy.myfirstlambda.repository.TitleAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private TestDao testDao;

    @Autowired
    private TitleAuthorRepository titleAuthorRepository;

    @Autowired
    DynamoDBQueryCreator<TitleAuthorEntity> dynamoDBQueryCreator;

    @RequestMapping(value = "/createEntity", method = {RequestMethod.POST})
    public TitleAuthorEntity createEntity(@RequestBody DemoRequest request) {
        TitleAuthorEntity entity = new TitleAuthorEntity();
        entity.setTitle(request.getTitle());
        entity.setAuthor(request.getAuthor());
        entity.setDescription(request.getDescription());
        titleAuthorRepository.save(entity);

        List<TitleAuthorEntity> ret = dynamoDBQueryCreator
                                        .addCriteria(String.class, "SIMPLE_PROPERTY", "title", Arrays.asList((Object)"test1").iterator())
                                        .and(String.class, "SIMPLE_PROPERTY", "author", Arrays.asList((Object)"test1").iterator())
                                        .complete(null);
        return entity;
    }

    @RequestMapping(value = "/deleteEntityOnlyHashKey", method = {RequestMethod.POST})
    public void deleteEntityOnlyHashKey(@RequestBody DemoRequest request) {
        TitleAuthorEntity entity = new TitleAuthorEntity();
        entity.setTitle(request.getTitle());
        testDao.deleteEntity(entity);
    }

    @RequestMapping(value = "/deleteEntityWithRangeKey", method = {RequestMethod.POST})
    public void deleteEntityWithRangeKey(@RequestBody DemoRequest request) {
        TitleAuthorEntity entity = new TitleAuthorEntity();
        entity.setTitle(request.getTitle());
        entity.setAuthor(request.getAuthor());
        testDao.deleteEntity(entity);
    }

    @RequestMapping(value = "/getEntityOnlyHashKey", method = {RequestMethod.POST})
    public TitleAuthorEntity getEntityOnlyHashKey(@RequestBody DemoRequest request) {
        TitleAuthorEntity entity = testDao.getDemoEntity(request.getTitle());
        System.out.println(entity.getTitle());

        return entity;
    }

    @RequestMapping(value = "/getEntityWithRangeKey", method = {RequestMethod.POST})
    public TitleAuthorEntity getEntityWithRangeKey(@RequestBody DemoRequest request) {
        TitleAuthorEntity entity = testDao.getDemoEntity(request.getTitle(), request.getAuthor());
        return entity;
    }

    @RequestMapping(value = "/getEntityList", method = {RequestMethod.POST})
    public void getEntityList(@RequestBody DemoRequest request) {
        //List<DynamoDemoEntity> entity = testDao.getDemoEntityList(request.getTitle());
        testDao.getDemoEntityListUsingQueryPage(request.getTitle());
    }

    @RequestMapping(value = "/getEntityListByQuery", method = {RequestMethod.POST})
    public void getEntityWithLimit(@RequestBody DemoRequest request) {
        testDao.getDemoEntityListUsingScanPage(request.getTitle());
        //return entity;
    }

    @RequestMapping(value = "/getCount", method = {RequestMethod.POST})
    public Integer getCount(@RequestBody DemoRequest request) {
        int count = testDao.getCountDemoEntityList(request.getTitle());
        return count;
    }
}
