CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE notebook_sku (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    page_count INTEGER NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE notebook_instance (
    id BIGSERIAL PRIMARY KEY,
    sku_id BIGINT NOT NULL REFERENCES notebook_sku(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    qr_hash VARCHAR(255) UNIQUE NOT NULL,
    redeemed_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE pages (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    notebook_instance_id BIGINT REFERENCES notebook_instance(id),
    image_url VARCHAR(255) NOT NULL,
    client_page_id VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT uq_user_client_page UNIQUE (user_id, client_page_id)
);

CREATE TABLE ocr_jobs (
    id BIGSERIAL PRIMARY KEY,
    page_id BIGINT NOT NULL REFERENCES pages(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    status VARCHAR(255) NOT NULL,
    reserved_coins INTEGER NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE ocr_results (
    id BIGSERIAL PRIMARY KEY,
    page_id BIGINT NOT NULL REFERENCES pages(id) UNIQUE,
    extracted_text TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE coin_ledger (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    transaction_type VARCHAR(255) NOT NULL,
    amount INTEGER NOT NULL,
    reason VARCHAR(255),
    reference_id VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
