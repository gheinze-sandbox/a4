DROP TYPE IF EXISTS user_account_status CASCADE;
CREATE TYPE user_account_status AS ENUM('ACTIVE', 'LOCKED', 'RETIRED');

-- DROP TYPE IF EXISTS user_account_limited CASCADE;
-- CREATE TYPE user_account_limited AS (
--    id            bigint
--   ,name          character varying(64)
--   ,status        user_account_status
--   ,display_name  character varying(64)
--   ,email         character varying(64)
-- );



CREATE TABLE user_account(
  fully_qualified_account_name  character varying(64)  NOT NULL
 ,encrypted_password            character varying(256) NOT NULL
 ,status             user_account_status    NOT NULL DEFAULT 'ACTIVE'::user_account_status
 ,display_name       character varying(64)  NOT NULL
 ,email_address      character varying(64)
) INHERITS(base);

COMMENT ON TABLE user_account IS 'All operations performed in the application are associated with a "user_account" (a data owner). The user must authenticate in order to use system services and has access to data associated with the account.';

COMMENT ON COLUMN user_account.encrypted_password IS 'The encrypted password used to log into this user account.';
COMMENT ON COLUMN user_account.status IS 'The status of an account will affect the operations it may perform. An account my be "ACTIVE", "LOCKED", "RETIRED", etc';
COMMENT ON COLUMN user_account.display_name IS 'The name to use for display or greeting purposes.';


-- Add keys:

SELECT ist_pk('user_account');
SELECT ist_bk('user_account', ARRAY['fully_qualified_account_name']);
SELECT ist_org_index('user_account');
