create table if not exists users
(
    id         bigserial primary key,
    name       varchar(255)                              not null,
    email      varchar(255)                default null unique,
    phone      varchar(255)                default null,
    password   varchar(255)                default null,
    role       varchar(100)                default null,
    created_at timestamp without time zone default now() not null,
    blocked_at timestamp without time zone default null,
    updated_at timestamp without time zone default null,
    deleted_at timestamp without time zone default null
);


create table if not exists category
(
    id                 bigserial primary key,
    name               varchar(255)                null,
    deleted_at         timestamp without time zone null,
    parent_category_id bigint                      null,
    foreign key (parent_category_id) references category (id)
);

create sequence if not exists product_seq increment by 50;

create table if not exists products
(
    id             bigint                      not null unique default nextval('product_seq'),
    name           varchar(255)                not null,
    description    text                        null            default '',
    price          double precision            null            default 0,
    stock_quantity int                         null            default 0,
    created_at     timestamp without time zone                 default now() not null,
    deleted_at     timestamp without time zone null,
    category_id    bigint                      null,
    author_id      bigint                      not null,
    image_url      varchar(255)                null,
    foreign key (category_id) references category (id),
    foreign key (author_id) references users (id)
);

create table if not exists cart
(
    id         bigserial primary key,
    user_id    bigint unique,
    updated_at timestamp without time zone null,
    foreign key (user_id) references users (id)
);

create table if not exists cart_item
(
    id         bigserial primary key,
    quantity   int default 0,
    product_id bigint not null,
    cart_id    bigint not null,
    foreign key (product_id) references products (id),
    foreign key (cart_id) references cart (id)
);

create table if not exists orders
(
    id           bigserial primary key,
    order_status varchar(200)                              null,
    created_at   timestamp without time zone default now() not null,
    updated_at   timestamp without time zone               null,
    user_id      bigint,
    foreign key (user_id) references users (id)
);

create sequence if not exists order_item_seq increment by 25;

create table if not exists order_item
(
    id         bigint not null unique default nextval('order_item_seq'),
    order_id   bigint not null,
    product_id bigint not null,
    quantity   int                    default 0,
    foreign key (order_id) references orders (id),
    foreign key (product_id) references products (id)
);

create table if not exists refresh_token
(
    id          bigserial primary key,
    token       varchar(255) not null,
    user_id     bigint       not null,
    expiry_date timestamp with time zone,
    foreign key (user_id) references users (id)
)
