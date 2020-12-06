CREATE TABLE public.quotation
(
    uuid uuid NOT NULL,
    culture character varying(30) NOT NULL,
    date date NOT NULL,
    value numeric(10, 3) NOT NULL,
    original_value numeric(10, 3) NOT NULL,
    currency_quotation numeric(10, 3) NOT NULL,
    PRIMARY KEY (uuid)
);