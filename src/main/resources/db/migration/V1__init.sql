CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        customer_email TEXT NOT NULL,
                        amount_cents BIGINT NOT NULL,
                        status TEXT NOT NULL,
                        created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
