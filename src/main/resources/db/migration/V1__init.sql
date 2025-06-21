create table if not exists users
(
    id         bigserial primary key,
    name       varchar(255) not null,
    email      varchar(255) unique,
    phone      varchar(255),
    password   varchar(255),
    role       varchar(100),
    created_at timestamp default now(),
    blocked_at timestamp    null,
    updated_at timestamp    null,
    deleted_at timestamp    null
);


create table if not exists category
(
    id                 bigserial primary key,
    name               varchar(255)                null,
    deleted_at         timestamp without time zone null,
    parent_category_id bigint                      null,
    foreign key (parent_category_id) references category (id)
);

create table if not exists products
(
    id          bigserial primary key,
    name        varchar(255) not null,
    description text         null default '',
    image_url   varchar(255) null,
    created_at  timestamp         default now(),
    deleted_at  timestamp    null,
    category_id bigint       null,
    author_id   bigint       not null,
    foreign key (category_id) references category (id),
    foreign key (author_id) references users (id)
);

--необходимо на уровне сервиса реализовать функционал: один author_id не может иметь дубликат product_id в таблице inventory
--пример (таблица inventory):
--первая строка в таблице имеет author_id 1 и product_id 1
--вторая строка в таблице имеет author_id 1 и product_id 1 <- ошибка, один авторам может иметь разное product_id, нельзя дублировать product_id для конкретного author_id
create table if not exists inventory
(
    id         bigserial primary key,
    product_id bigint       not null,
    broker     varchar(255) not null,        --названия брокер/поставщик товара/компания
    author_id  bigint       not null unique, --deliver id, unique для того чтобы
    quantity   int          not null,
    price      double precision default 0,
    created_at timestamp        default now(),
    updated_at timestamp,
    deleted_at timestamp,
    foreign key (product_id) references products (id),
    foreign key (author_id) references users (id)
);

-- хранить текущий сеанс товаров в корзине, после удаляем при order -е
create table if not exists cart
(
    id           bigserial primary key,
    quantity     int default 0,
    inventory_id bigint    not null,
    product_id   bigint    not null,                                 --своего рода изначальная декомпозиция, цель такой ссылки,
    -- быстрый доступ с одним join нежели чем с двумя join -ами (join inventory join products)
    user_id      bigint,
    created_at   timestamp default now(), -- мб эти три колонки пригодятся
    updated_at   timestamp null,
    deleted_at   timestamp null,                                     --если будем делать hard_delete, то оставляем логику с unique constraint,
    -- если нет - убираем constraint и оставляем в бд как архив (как такового смысла не делать hard delete - не вижу)
    foreign key (inventory_id) references inventory (id),            --Через join будем брать саму мета информацию продукта
    -- и конкретного поставщика
    foreign key (user_id) references users (id),
    foreign key (product_id) references products (id),
    constraint user_cart unique (product_id, user_id, inventory_id), -- таблица не может иметь дубликат product_id, user_id, inventory_id,
-- вместо дублирования будем делать увелечения значения quantity;
    constraint positive_quantity check (quantity >= 0)
);

create sequence if not exists orders_seq increment by 10;

--Используем в качестве архива если статус DELIVERED + current_timestamp > created_at + interval '1 month',
-- все остальные отображаем в страничке заказов, unique constraint -а НЕТУ потому что заказы могут дублироваться
create table if not exists orders
(
    id           bigint       not null unique default nextval('orders_seq'),
    product_id   bigint       not null,
    quantity     int                          default 0,
    order_status varchar(200) null,
    created_at   timestamp                    default now(),
    updated_at   timestamp    null,
    user_id      bigint,
    foreign key (product_id) references products (id),
    foreign key (user_id) references users (id)
);

create table if not exists refresh_token
(
    id          bigserial primary key,
    token       varchar(255) not null,
    user_id     bigint       not null,
    expiry_date timestamp with time zone, --один user один refresh_token
    constraint owner unique (user_id),
    foreign key (user_id) references users (id)
)
