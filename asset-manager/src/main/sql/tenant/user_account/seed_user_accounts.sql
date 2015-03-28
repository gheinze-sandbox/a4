-- Create an admin account "a4admin" with password "a42day"

DO $$
DECLARE

    v_org_id   organization.id%TYPE;
    v_admin_id user_account.id%TYPE;

BEGIN

    INSERT INTO organization(org_name, email_address)
      VALUES('Public', 'glenn@gheinze.com')
      RETURNING id into v_org_id
    ;


    INSERT INTO user_account(org_id, fully_qualified_account_name, encrypted_password, status, display_name, email_address)
      VALUES (v_org_id, 'a4admin@Public', '$2a$10$qCNNvYElESfEHoMe1eYixOLihYr63ELzMFPSyT1SblJWVAxXMXfga', 'ACTIVE', 'Admin', 'glenn@gheinze.com')
      RETURNING id INTO v_admin_id
    ;

    INSERT INTO user_account_authority(org_id, user_account_id, authority)
      VALUES (v_org_id, v_admin_id, 'ROLE_ADMIN')
    ;

    INSERT INTO user_account(org_id, fully_qualified_account_name, encrypted_password, status, display_name, email_address)
      VALUES (v_org_id, 'guest@Public', '$2a$10$9uCUCRtHZamr.CE/tn/XL.K1Gq.A2IO7x5XbcuzviIn6blFWTooNy', 'ACTIVE', 'Admin', 'glenn@gheinze.com')
      RETURNING id INTO v_admin_id
    ;

    INSERT INTO user_account_authority(org_id, user_account_id, authority)
      VALUES (v_org_id, v_admin_id, 'ROLE_USER')
    ;

END
$$;
