package impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.custom.PostRepositoryCustom;
import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.QCategory;
import com.sparta.deventer.entity.QPost;
import com.sparta.deventer.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findAllByCategory(Category category, Pageable pageable) {
        QPost qPost = QPost.post;
        QCategory qCategory = QCategory.category;
        List<Post> postList = queryFactory
                .select(qPost)
                .join(qPost.category, qCategory)
                .where(qCategory.eq(category))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(qPost)
                .join(qPost.category, qCategory)
                .where(qCategory.eq(category))
                .fetch().size();

        return new PageImpl<>(postList, pageable, total);
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        QPost qPost = QPost.post;
        List<Post> postList = queryFactory
                .select(qPost)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .select(qPost)
                .fetch().size();

        return new PageImpl<>(postList, pageable, total);
    }

    @Override
    public Page<Post> findByUserId(Long userId, Pageable pageable) {
        QPost qPost = QPost.post;
        QUser qUser = QUser.user;

        List<Post> postList = queryFactory
                .select(qPost)
                .join(qPost.user, qUser)
                .where(qUser.id.eq(userId))
                .fetch();
        long total = queryFactory
                .select(qPost)
                .join(qPost.user, qUser)
                .where(qUser.id.eq(userId))
                .fetch().size();

        return new PageImpl<>(postList, pageable, total);
    }

    @Override
    public List<Post> findByUserId(Long userId) {
        QPost qPost = QPost.post;
        QUser qUser = QUser.user;

        return queryFactory
                .select(qPost)
                .join(qPost.user, qUser)
                .where(qUser.id.eq(userId))
                .fetch();
    }

}
