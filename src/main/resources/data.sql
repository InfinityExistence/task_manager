INSERT INTO public.user_details VALUES ('manager@gmail.com', '$2a$10$aDqJaFVM3iNtO1pQH/Y15OFV.9xYztteO/Cx/3Vz0a0Yy1UW4NeN.') ON CONFLICT DO NOTHING;
INSERT INTO public.user_details VALUES ('programmer@gmail.com', '$2a$10$45wrwprDxB8cdJaK3YspruYGF0nMzX2iZALVk.k/X0NCCFfjkQcA6') ON CONFLICT DO NOTHING;
INSERT INTO public.user_details VALUES ('tester@gmail.com', '$2a$10$Xiv81.AMvS/Ql/6sLnuyx.B1yzkJW4SPvLtWNCtr1Hrcy/q2a73o6') ON CONFLICT DO NOTHING;



INSERT INTO public.user_data VALUES ('0475cca7-986d-43e1-aacf-76160d4c5f7d', 'manager', 'manager@gmail.com') ON CONFLICT DO NOTHING;
INSERT INTO public.user_data VALUES ('4b79e6d2-1d62-4efe-b7fc-ce47fc89a53a', 'programmer', 'programmer@gmail.com') ON CONFLICT DO NOTHING;
INSERT INTO public.user_data VALUES ('91af5e53-86e1-47bf-8b9c-37ea3117d10e', 'tester', 'tester@gmail.com') ON CONFLICT DO NOTHING;



INSERT INTO public.task VALUES ('de3b9b44-f8bd-445e-9eed-8a9a3f737e80', 'Description task Program 2', 2, 1, 'Task Program 2', '4b79e6d2-1d62-4efe-b7fc-ce47fc89a53a', '0475cca7-986d-43e1-aacf-76160d4c5f7d') ON CONFLICT DO NOTHING;
INSERT INTO public.task VALUES ('c763f8ca-34eb-448e-b071-bf8891b6b42e', 'Description task Program 3', 2, 1, 'Task Program 3', '4b79e6d2-1d62-4efe-b7fc-ce47fc89a53a', '0475cca7-986d-43e1-aacf-76160d4c5f7d') ON CONFLICT DO NOTHING;
INSERT INTO public.task VALUES ('e3be5948-8a7a-40da-8210-24976e57072f', 'Description task Test 4', 1, 0, 'Task Test 4', '91af5e53-86e1-47bf-8b9c-37ea3117d10e', '0475cca7-986d-43e1-aacf-76160d4c5f7d') ON CONFLICT DO NOTHING;
INSERT INTO public.task VALUES ('02af9e0e-93e2-4294-8786-cbb00151f38f', 'Description task Test 5', 1, 0, 'Task Test 5', '91af5e53-86e1-47bf-8b9c-37ea3117d10e', '0475cca7-986d-43e1-aacf-76160d4c5f7d') ON CONFLICT DO NOTHING;
INSERT INTO public.task VALUES ('00149751-c2ad-4466-b2a8-d0899e7948f6', 'Description task Program 1', 0, 2, 'Task Program 1', '4b79e6d2-1d62-4efe-b7fc-ce47fc89a53a', '0475cca7-986d-43e1-aacf-76160d4c5f7d') ON CONFLICT DO NOTHING;
INSERT INTO public.task VALUES ('fd93077b-af96-4cd7-8a2b-721d9abd8a7b', 'Description task Test 6', 1, 1, 'Task Test 6', '91af5e53-86e1-47bf-8b9c-37ea3117d10e', '0475cca7-986d-43e1-aacf-76160d4c5f7d') ON CONFLICT DO NOTHING;



INSERT INTO public.comment VALUES ('e8a73051-64a6-423d-983e-f6685fc797d3', 'Fun bug!', '2024-08-09 03:08:00.323088', '4b79e6d2-1d62-4efe-b7fc-ce47fc89a53a', 'fd93077b-af96-4cd7-8a2b-721d9abd8a7b') ON CONFLICT DO NOTHING;
INSERT INTO public.comment VALUES ('ec00a306-d95d-41eb-ab26-8f68cf43e551', 'Comment 124!', '2024-08-09 03:12:03.967347', '4b79e6d2-1d62-4efe-b7fc-ce47fc89a53a', 'e3be5948-8a7a-40da-8210-24976e57072f') ON CONFLICT DO NOTHING;
INSERT INTO public.comment VALUES ('b61068a4-f2a8-4091-9105-bae8ec12d747', 'Comment 321!', '2024-08-09 03:12:23.456631', '4b79e6d2-1d62-4efe-b7fc-ce47fc89a53a', '02af9e0e-93e2-4294-8786-cbb00151f38f') ON CONFLICT DO NOTHING;
INSERT INTO public.comment VALUES ('f06e5685-14d6-4282-95c3-e9621e23d29c', 'Fix', '2024-08-09 03:12:57.708491', '4b79e6d2-1d62-4efe-b7fc-ce47fc89a53a', 'fd93077b-af96-4cd7-8a2b-721d9abd8a7b') ON CONFLICT DO NOTHING;
INSERT INTO public.comment VALUES ('29e0c226-4bfb-4d53-a025-498522164c04', '113123', '2024-08-09 03:18:24.623725', '4b79e6d2-1d62-4efe-b7fc-ce47fc89a53a', 'fd93077b-af96-4cd7-8a2b-721d9abd8a7b') ON CONFLICT DO NOTHING;
INSERT INTO public.comment VALUES ('d5650c37-e8c2-4799-9e1d-d4b8a85cc773', 'Ok', '2024-08-09 03:25:13.31846', '4b79e6d2-1d62-4efe-b7fc-ce47fc89a53a', 'de3b9b44-f8bd-445e-9eed-8a9a3f737e80') ON CONFLICT DO NOTHING;

