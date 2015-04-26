CREATE TABLE user_account_authority(
  user_account_id  bigint NOT NULL CONSTRAINT uaa_user_account_id_fk REFERENCES user_account(id)
 ,authority        character varying(64)  NOT NULL
) INHERITS(base);

COMMENT ON TABLE user_account_authority IS 'Roles granted to a user to provide permissions to access services and ui elements.';

COMMENT ON COLUMN user_account_authority.user_account_id IS 'The user granted the role.';
COMMENT ON COLUMN user_account_authority.authority IS 'The role granted to this user.';


-- Add keys:

SELECT ist_pk('user_account_authority');

SELECT ist_bk('user_account_authority', ARRAY['user_account_id', 'authority']);
