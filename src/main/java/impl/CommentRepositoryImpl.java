package impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.custom.CommentRepositoryCustom;
import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.QComment;
import com.sparta.deventer.entity.QPost;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findAllByPostId(long id) {
        QComment qComment = QComment.comment;
        QPost qPost = QPost.post;

        return queryFactory.select(qComment)
                .join(qComment.post, qPost).fetch();
    }

    @Override
    public Page<Comment> findByUserId(Long userId, Pageable pageable) {
        QComment qComment = QComment.comment;
        List<Comment> commentList = queryFactory
                .select(qComment)
                .where(qComment.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .select(qComment)
                .where(qComment.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch().size();
        return new PageImpl<>(commentList, pageable, total);
    }
}
